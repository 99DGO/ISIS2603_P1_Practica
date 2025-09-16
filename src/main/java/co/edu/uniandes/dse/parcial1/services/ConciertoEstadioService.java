package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConciertoEstadioService {

    // Complete

    @Autowired
    private EstadioRepository estadioRepository;

    @Autowired
    private ConciertoRepository conciertoRepository;

    @Transactional
    public ConciertoEntity addEstadio(Long conciertoId, Long estadioId) throws EntityNotFoundException {
        Optional<EstadioEntity> estadioEntity = estadioRepository.findById(estadioId);
        if (estadioEntity.isEmpty())
            throw new EntityNotFoundException("Estadio no encontrado");

        Optional<ConciertoEntity> conciertoEntity = conciertoRepository.findById(conciertoId);
        if (conciertoEntity.isEmpty())
            throw new EntityNotFoundException("Concierto no encontrada");

        if (conciertoEntity.getPresupuesto() < estadioEntity.getPrecioAlquiler())
            throw new EntityNotFoundException("Supera presupuesto");
        
        if (conciertoEntity.getAforo() > estadioEntity.getAforo())
            throw new EntityNotFoundException("Supera aforo");
        

        for (int i =0; i< estadioEntity.getConciertos().size(); i++) {
            ConciertoEntity conciertoEntity2;
            if (Duration.between(conciertoEntity2.getFecha(), conciertoEntity.getFecha())< Duration.ofDays(2))
                throw new EntityNotFoundException("Fecha invalida");
        }

        conciertoEntity.get().setEstadio(estadioEntity.get());
        return conciertoEntity.get();
    }

}
