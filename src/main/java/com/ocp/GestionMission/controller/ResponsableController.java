package com.ocp.GestionMission.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ocp.GestionMission.model.Responsable;
import com.ocp.GestionMission.repository.ResponsableRepository;

@RestController
@RequestMapping("/api/responsable")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ResponsableController {
    private final Path fileStorageLocation;

    @Autowired
    private ResponsableRepository responsableRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public ResponsableController() throws IOException {
        this.fileStorageLocation = Paths.get("uploaded-files").toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = fileStorageLocation.resolve(filename).normalize();
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Responsable>> getAllResponsables() {
        List<Responsable> list = responsableRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Responsable> getThisResponsable(@PathVariable Long id) {
        Optional<Responsable> responsable = responsableRepository.findById(id);
        return responsable.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                            .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PostMapping("/")
    public ResponseEntity<Responsable> saveResponsable(@RequestBody Responsable responsable) {
        String encodedPassword = passwordEncoder.encode(responsable.getMotDePasse());
        responsable.setMotDePasse(encodedPassword);
        responsableRepository.save(responsable);
        return ResponseEntity.status(HttpStatus.CREATED).body(responsable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Responsable> editResponsable(@PathVariable Long id, @RequestBody Responsable responsable) {
        Optional<Responsable> existingResponsable = responsableRepository.findById(id);
        if (!existingResponsable.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        responsable.setId(id);
        if (responsable.getMotDePasse() != null && !responsable.getMotDePasse().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(responsable.getMotDePasse());
            responsable.setMotDePasse(encodedPassword);
        } else {
            responsable.setMotDePasse(existingResponsable.get().getMotDePasse());
        }
        responsableRepository.save(responsable);
        return ResponseEntity.status(HttpStatus.OK).body(responsable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponsable(@PathVariable Long id) {
        try {
            Optional<Responsable> responsableOptional = responsableRepository.findById(id);
            if (!responsableOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Responsable responsable = responsableOptional.get();

            
            String filename = responsable.getImageUrl();
            if (filename != null && !filename.isEmpty()) {
                Path file = fileStorageLocation.resolve(filename).normalize();
                Files.deleteIfExists(file);
            }

           
            responsableRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(filename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
        return ResponseEntity.ok(filename);
    }
    @DeleteMapping("/image/{filename:.+}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
        Path file = fileStorageLocation.resolve(filename).normalize();
        try {
            Files.deleteIfExists(file);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
