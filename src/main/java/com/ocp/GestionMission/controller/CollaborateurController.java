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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ocp.GestionMission.model.Collaborateur;
import com.ocp.GestionMission.repository.CollaborateurRepository;

@RestController
@RequestMapping("/api/collaborateur")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CollaborateurController {
    private final Path fileStorageLocation;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    public CollaborateurController() throws IOException {
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
    public ResponseEntity<List<Collaborateur>> getAllCollaborateurs() {
        List<Collaborateur> list = collaborateurRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collaborateur> getThisCollaborateur(@PathVariable Long id) {
        Optional<Collaborateur> collaborateur = collaborateurRepository.findById(id);
        return collaborateur.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                            .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PostMapping("/")
    public ResponseEntity<Collaborateur> saveCollaborateur(@RequestBody Collaborateur collaborateur) {
        collaborateurRepository.save(collaborateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(collaborateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collaborateur> editCollaborateur(@PathVariable Long id, @RequestBody Collaborateur collaborateur) {
        Optional<Collaborateur> existingCollaborateur = collaborateurRepository.findById(id);
        if (!existingCollaborateur.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        collaborateur.setId(id);
        collaborateurRepository.save(collaborateur);
        return ResponseEntity.status(HttpStatus.OK).body(collaborateur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollaborateur(@PathVariable Long id) {
        try {
            Optional<Collaborateur> collaborateurOptional = collaborateurRepository.findById(id);
            if (!collaborateurOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Collaborateur collaborateur = collaborateurOptional.get();

            // Suppression de l'image associée si elle existe
            String filename = collaborateur.getImageUrl();
            if (filename != null && !filename.isEmpty()) {
                Path file = fileStorageLocation.resolve(filename).normalize();
                Files.deleteIfExists(file);
            }

            // Suppression du collaborateur
            collaborateurRepository.deleteById(id);

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
