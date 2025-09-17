package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.ConciertoEntity;
import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion Prize - Author
 *
 * @author ISIS2603
 */
@DataJpaTest
@Transactional
@Import(ConciertoEstadioService.class)
class ConciertoEstadioServiceTest {

	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private ConciertoEstadioService conciertoEstadioService;

	@Autowired
	private TestEntityManager entityManager;

	private List<EstadioEntity> estadioList = new ArrayList<>();
	private List<ConciertoEntity> conciertoList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		insertData();
		clearData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ConciertoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from EstadioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			EstadioEntity estadio = factory.manufacturePojo(EstadioEntity.class);
			ConciertoEntity concierto = factory.manufacturePojo(ConciertoEntity.class);

			estadio.setAforo(2002);
			estadio.setPrecioAlquiler(2000000L);

			LocalDateTime now = LocalDateTime.now().plusMonths(i);
			LocalDateTime futureDateTime = now.plusYears(1);

			concierto.setFecha(futureDateTime);
			concierto.setAforo(2000);
			concierto.setPresupuesto(90000000L);

			estadio.getConciertos().add(concierto);

			entityManager.persist(estadio);
			entityManager.persist(concierto);
			conciertoList.add(concierto);
			estadioList.add(estadio);
		}

		LocalDateTime now = LocalDateTime.now().plusMonths(5);
		LocalDateTime futureDateTime = now.plusYears(1);

		ConciertoEntity concierto = factory.manufacturePojo(ConciertoEntity.class);
		concierto.setFecha(futureDateTime);
		concierto.setAforo(2000);
		concierto.setPresupuesto(90000000L);
		entityManager.persist(concierto);
		conciertoList.add(concierto);
	}

	@Test
	void testAddEstadio() throws EntityNotFoundException, IllegalOperationException {
		EstadioEntity estadioEntity = estadioList.get(0);
		ConciertoEntity conciertoEntity = conciertoList.get(3);
		EstadioEntity response = conciertoEstadioService.addEstadio(conciertoEntity.getId(), estadioEntity.getId() );

		assertNotNull(response);
		assertEquals(estadioEntity.getId(), response.getId());
	}

	@Test
	void testAddInvalidEstadio() {
		assertThrows(EntityNotFoundException.class, () -> {
			ConciertoEntity conciertoEntity = conciertoList.get(0);
			conciertoEstadioService.addEstadio(conciertoEntity.getId(), 0L);
		});
	}

	@Test
	void testAddEstadioInvalidConcierto() {
		assertThrows(EntityNotFoundException.class, () -> {
			EstadioEntity entity = estadioList.get(0);
			conciertoEstadioService.addEstadio(0L, entity.getId());
		});
	}

	@Test
	void testAddEstadioInvalidConciertoFechas() {
		assertThrows(IllegalOperationException.class, () -> {
			LocalDateTime now = LocalDateTime.now();

			EstadioEntity entity = estadioList.get(0);
			ConciertoEntity conciertoEntity = conciertoList.get(0);
			conciertoEntity.setFecha(now.plusYears(1));
			ConciertoEntity conciertoEntity2 = conciertoList.get(1);
			conciertoEntity2.setFecha(now.plusYears(1));
			
			EstadioEntity response = conciertoEstadioService.addEstadio(conciertoEntity.getId(), entity.getId());
			EstadioEntity response2 = conciertoEstadioService.addEstadio(conciertoEntity2.getId(), entity.getId());

		});
	}

}