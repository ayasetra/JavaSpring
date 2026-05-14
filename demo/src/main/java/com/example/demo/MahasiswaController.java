package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/mahasiswa")
@CrossOrigin(origins = "*")
public class MahasiswaController {

    @Autowired
    private MahasiswaRepository repository;

    // GET: Mengambil data mahasiswa (termasuk foto)
    @GetMapping("/{nim}")
    public ResponseEntity<Mahasiswa> getMahasiswa(@PathVariable String nim) {
        return repository.findById(nim)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST/UPDATE: Menyimpan atau memperbarui data dan foto
    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("nim") String nim,
            @RequestParam("nama") String nama,
            @RequestParam("foto") MultipartFile file) throws IOException {

        Mahasiswa mhs = repository.findById(nim).orElse(new Mahasiswa());
        mhs.setNimMhs(nim);
        mhs.setNamaMhs(nama);
        mhs.setFotoMhs(file.getBytes()); // Mengubah file menjadi byte array (Blob)

        repository.save(mhs);
        return ResponseEntity.ok("Data berhasil diperbarui");
    }
}