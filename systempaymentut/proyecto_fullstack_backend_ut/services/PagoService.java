package com.systempaymentut.proyecto_fullstack_backend_ut.services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Estudiante;
import com.systempaymentut.proyecto_fullstack_backend_ut.entities.Pago;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.PagoStatus;
import com.systempaymentut.proyecto_fullstack_backend_ut.enums.TypePago;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.EstudianteRepository;
import com.systempaymentut.proyecto_fullstack_backend_ut.repository.PagoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional //Para asegurar que los métodos de la clase se ejecuten dentro de una transacción

public class PagoService {
    
    // Inyección de dependencias de PagoRepository para interactuar con la bd de pagos
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;
    
    /**
     * 
     * @param file
     * @param cantidad
     * @param type
     * @param date
     * @param codigoEstudiante
     * @return
     * @throws IOException
     */

    public Pago savePago(MultipartFile file, double cantidad, TypePago type, LocalDate date, String codigoEstudiante)
        throws IOException {

        /**
        | * construir la ruta donde se guardará el archivo dentro del sistema
        | * System.getProperty("user.home"): obtiene la ruta del directorio personal del usuario actual del SO
        | * Paths.get : construir una ruta dentro del directorio personl en la carpeta "enset-data/pagos"
        | *
        | */
        Path folderPath = Paths.get(System.getProperty("user.home"), "enset.data", "pagos");

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

    String fileName = UUID.randomUUID().toString();

    Path filePath = Paths.get(System.getProperty("user.home"), "enset.data", "pagos", fileName + ".pdf");

    Files.copy(file.getInputStream(), filePath);

    Estudiante estudiante = estudianteRepository.findByCodigo(codigoEstudiante);

            Pago pago = Pago.builder()
            .type(type)
            .status(PagoStatus.CREADO)
            .fecha(date)
            .estudiante(estudiante)
            .cantidad(cantidad)
            .file(filePath.toUri().toString())
                .build();

            return pagoRepository.save(pago);

    }

    public byte[] getArchivoPorId(long PagoId) throws IOException {

        Pago pago = pagoRepository.findById(PagoId).get();
        
        
        return Files.readAllBytes(Path.of(URI.create(pago.getFile())));
        
    }

    public Pago actualizarPagoPorStatus(PagoStatus status, Long id){

        Pago pago = pagoRepository.findById(id).get();

        pago.setStatus(status);
    
        return pagoRepository.save(pago);

    }

}
    

