package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
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
    public EstadioEntity addEstadio(Long conciertoId, Long estadioId) throws EntityNotFoundException, IllegalOperationException {
        Optional<EstadioEntity> estadioEntity1 = estadioRepository.findById(estadioId);
        if (estadioEntity1.isEmpty())
            throw new EntityNotFoundException("Estadio no encontrado");

        Optional<ConciertoEntity> conciertoEntity1 = conciertoRepository.findById(conciertoId);
        if (conciertoEntity1.isEmpty())
            throw new EntityNotFoundException("Concierto no encontrada");

        ConciertoEntity conciertoEntity= conciertoEntity1.get();
        EstadioEntity estadioEntity= estadioEntity1.get();

        if (conciertoEntity.getPresupuesto() < estadioEntity.getPrecioAlquiler())
            throw new IllegalOperationException("Supera presupuesto");
        
        if (conciertoEntity.getAforo() > estadioEntity.getAforo())
            throw new IllegalOperationException("Supera aforo");
        

        for (int i =0; i< estadioEntity.getConciertos().size(); i++) {
            ConciertoEntity conciertoEntity2 = estadioEntity.getConciertos().get(i);

            Duration duration= Duration.between(conciertoEntity2.getFecha(), conciertoEntity.getFecha());
            long diffInDays = duration.toDays();

            if (diffInDays<2)
                throw new IllegalOperationException("Fecha invalida");
        }

        conciertoEntity.setEstadio(estadioEntity);
        return estadioEntity;
    }

}
