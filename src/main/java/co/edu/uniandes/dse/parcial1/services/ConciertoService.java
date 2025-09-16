package co.edu.uniandes.dse.parcial1.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.ConciertoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConciertoService {

    // Complete

    @Autowired 
    ConciertoRepository conciertoRepository;

    @Transactional
    public ConciertoEntity createConcierto(ConciertoEntity concierto) throws IllegalOperationException
    {
        LocalDateTime hoy = LocalDateTime.now();
        //fecha no puede estar en el pasado
        if( concierto.getFecha().isBefore(hoy)) {
            throw new IllegalOperationException("La fehca del concierto no puede ser el pasado");
        }

		// La capacidad del concierto debe ser un número superior a 10 personas
        if(concierto.getAforo() >10) {
            throw new IllegalOperationException("El número máximo de personas debe ser superior a 10");
        }

		//El presupuesto del concierto debe ser un número superior a 1000 dólares
        if(concierto.getPresupuesto() >1000) {
            throw new IllegalOperationException("El presupuesto debe ser mayor a 1000");
        }

        return conciertoRepository.save(concierto); 
    }

}
