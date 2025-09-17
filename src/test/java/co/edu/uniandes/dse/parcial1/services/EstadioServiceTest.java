package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uk.co.jemos.podam.api.PodamFactory;


@DataJpaTest
@Transactional
@Import(EstadioService.class)
public class EstadioServiceTest {

    @Autowired
	private EstadioService estadioService;

    @Autowired
	private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();


	@BeforeEach
	void setUp() {
		clearData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from EstadioEntity");
	}

    @Test
    void testCreateEstadio() throws IllegalOperationException {
		
        EstadioEntity newEntity = factory.manufacturePojo(EstadioEntity.class); //Creamos objeto con random test data

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime futureDateTime = now.plusYears(1);

		newEntity.setAforo(2000);
		newEntity.setPrecioAlquiler(2000000L);
		EstadioEntity result = estadioService.createEstadio(newEntity);
		assertNotNull(result); //Probamos que si se creo la entidad

		EstadioEntity entity = entityManager.find(EstadioEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getAforo(), entity.getAforo());
    }

	@Test
	void testCreateEstadioNoAforo() {
		assertThrows(IllegalOperationException.class, () -> {
			EstadioEntity newentity = factory.manufacturePojo(EstadioEntity.class);
			newentity.setAforo(0);

			estadioService.createEstadio(newentity);
		});
	}

}
