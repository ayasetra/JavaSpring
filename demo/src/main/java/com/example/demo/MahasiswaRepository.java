package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {
// Kosong saja! Spring Boot sudah otomatis membuatkan perintah
// SELECT, INSERT, UPDATE, don DELETE di balik layar.
}