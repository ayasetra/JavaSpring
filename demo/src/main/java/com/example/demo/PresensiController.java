package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/presensi")
@CrossOrigin("*")
public class PresensiController {

    @Autowired
    private PresensiRepository repository;

    @Autowired
    private GeoService geoService;

    // GET: History presensi dengan pagination (sesuai HistoryScreen.js)
    @GetMapping("/history/{nim}")
    public Page<Presensi> getHistory(
            @PathVariable String nim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repository.findByNimMhs(nim, pageable);
    }

    // POST: Simpan presensi baru (sesuai HomeScreen.js)
    @PostMapping
    public Presensi savePresensi(@RequestBody Presensi presensi) {
        return repository.save(presensi);
    }

    // PUT: Update data presensi
    @PutMapping("/{id}")
    public ResponseEntity<Presensi> updatePresensi(@PathVariable Long id, @RequestBody Presensi details) {
        Optional<Presensi> optionalPresensi = repository.findById(id);
        if (optionalPresensi.isPresent()) {
            Presensi existing = optionalPresensi.get();
            if (details.getKodeMk() != null) existing.setKodeMk(details.getKodeMk());
            if (details.getCourse() != null) existing.setCourse(details.getCourse());
            if (details.getDate() != null) existing.setDate(details.getDate());
            if (details.getJamPresensi() != null) existing.setJamPresensi(details.getJamPresensi());
            if (details.getPertemuanKe() != 0) existing.setPertemuanKe(details.getPertemuanKe());
            if (details.getStatus() != null) existing.setStatus(details.getStatus());
            if (details.getNimMhs() != null) existing.setNimMhs(details.getNimMhs());
            if (details.getRuangan() != null) existing.setRuangan(details.getRuangan());
            if (details.getDosenPengampu() != null) existing.setDosenPengampu(details.getDosenPengampu());
            return ResponseEntity.ok(repository.save(existing));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Cek lokasi (sesuai LocationScreen.js yang menggunakan lowercase /locate)
    @PostMapping("/locate")
    public String checkLocation(@RequestBody Map<String, Object> request) {
        try {
            double lat = Double.parseDouble(request.getOrDefault("lat", request.getOrDefault("Lat", "0")).toString());
            double lng = Double.parseDouble(request.getOrDefault("lng", request.getOrDefault("Lng", "0")).toString());

            boolean inside = geoService.isInside(lat, lng);
            return inside ? "IN AREA" : "OUT AREA";
        } catch (Exception e) {
            return "ERROR: Invalid coordinates";
        }
    }
}