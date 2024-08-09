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

import com.ocp.GestionMission.dto.StatusUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ocp.GestionMission.model.Vehicule;
import com.ocp.GestionMission.repository.VehiculesRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class VehiculeController {
    private final Path fileStorageLocation;

    @Autowired
    private VehiculesRepository vehiculesRepository;

    public VehiculeController() throws IOException {
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
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        List<Vehicule> list = vehiculesRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/vehicule/{id}")
    public ResponseEntity<Vehicule> getThisVehicule(@PathVariable String id) {
        Optional<Vehicule> vehicule = vehiculesRepository.findById(id);
        return vehicule.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PostMapping("/vehicule")
    public ResponseEntity<Vehicule> saveVehicule(@RequestBody Vehicule vehicule) {
        vehiculesRepository.save(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicule);
    }

    @PutMapping("/vehicule/{id}")
    public ResponseEntity<Vehicule> editVehicule(@PathVariable String id, @RequestBody Vehicule vehicule) {
        Optional<Vehicule> existingVehicule = vehiculesRepository.findById(id);
        if (!existingVehicule.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        vehicule.setVehiculeId(id);
        vehiculesRepository.save(vehicule);
        return ResponseEntity.status(HttpStatus.OK).body(vehicule);
    }

    @DeleteMapping("/vehicule/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable String id) {
        Optional<Vehicule> vehicule = vehiculesRepository.findById(id);
        if (!vehicule.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Vehicule vehiculeToDelete = vehicule.get();
        String imageUrl = vehiculeToDelete.getVehiculeImageUrl();
        try {
            Files.deleteIfExists(fileStorageLocation.resolve(imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        vehiculesRepository.delete(vehiculeToDelete);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/vehicule/{id}/status")
    public ResponseEntity<Vehicule> updateVehicleStatus(@PathVariable String id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        Optional<Vehicule> existingVehicule = vehiculesRepository.findById(id);
        if (!existingVehicule.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Vehicule vehicule = existingVehicule.get();
        vehicule.setStatus(statusUpdateRequest.getStatus()); // Assuming `status` is a field in `Vehicule`
        vehiculesRepository.save(vehicule);
        return ResponseEntity.status(HttpStatus.OK).body(vehicule);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String filename) {
        try {
            Path file = fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(file);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IOException ex) {
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
    @GetMapping("/search")
    public ResponseEntity<List<Vehicule>> searchVehicules(@RequestParam String query) {
        List<Vehicule> vehicules = vehiculesRepository.findByMarqueContainingOrModeleContainingOrImmatriculationContainingOrServiceContaining(query, query, query, query);
        return ResponseEntity.status(HttpStatus.OK).body(vehicules);
    }
}
