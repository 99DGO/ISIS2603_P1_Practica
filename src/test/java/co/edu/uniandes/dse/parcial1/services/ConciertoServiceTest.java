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
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uk.co.jemos.podam.api.PodamFactory;


@DataJpaTest
@Transactional
@Import(ConciertoService.class)
public class ConciertoServiceTest {

    @Autowired
	private ConciertoService conciertoService;

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
		entityManager.getEntityManager().createQuery("delete from ConciertoEntity");
	}

    @Test
    void testCreateConcierto() throws IllegalOperationException {
		
        ConciertoEntity newEntity = factory.manufacturePojo(ConciertoEntity.class); //Creamos objeto con random test data

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime futureDateTime = now.plusYears(1);

		newEntity.setFecha(futureDateTime);
		newEntity.setAforo(2000);
		newEntity.setPresupuesto(2000L);
		ConciertoEntity result = conciertoService.createConcierto(newEntity);
		assertNotNull(result); //Probamos que si se creo la entidad

		ConciertoEntity entity = entityManager.find(ConciertoEntity.class, result.getId());

		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getPresupuesto(), entity.getPresupuesto());
    }

	@Test
	void testCreateConciertoNoPresupuesto() {
		assertThrows(IllegalOperationException.class, () -> {
			ConciertoEntity newentity = factory.manufacturePojo(ConciertoEntity.class);
			newentity.setPresupuesto(0L);

			conciertoService.createConcierto(newentity);
		});
	}

}
