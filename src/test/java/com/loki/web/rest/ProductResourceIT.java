package com.loki.web.rest;

import static com.loki.web.rest.TestUtil.sameInstant;
import static com.loki.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.loki.IntegrationTest;
import com.loki.domain.Product;
import com.loki.domain.enumeration.ProductStatus;
import com.loki.repository.ProductRepository;
import com.loki.service.dto.ProductDTO;
import com.loki.service.mapper.ProductMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ProductStatus DEFAULT_PRODUCT_STATUS = ProductStatus.DRAFT;
    private static final ProductStatus UPDATED_PRODUCT_STATUS = ProductStatus.VALIDATED;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Double DEFAULT_QUANTITY_IN_STOCK = 1D;
    private static final Double UPDATED_QUANTITY_IN_STOCK = 2D;

    private static final Integer DEFAULT_NBR_OF_SELLS = 1;
    private static final Integer UPDATED_NBR_OF_SELLS = 2;

    private static final Double DEFAULT_MINIMAL_QUANTITY = 0D;
    private static final Double UPDATED_MINIMAL_QUANTITY = 1D;

    private static final Double DEFAULT_MAXIMAL_QUANTITY = 0D;
    private static final Double UPDATED_MAXIMAL_QUANTITY = 1D;

    private static final BigDecimal DEFAULT_WEIGHTED_AVERAGE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_WEIGHTED_AVERAGE_PRICE = new BigDecimal(1);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CONSUMPTION_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONSUMPTION_DEADLINE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BAR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_HALLWAY = "AAAAAAAAAA";
    private static final String UPDATED_HALLWAY = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DISPLAY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCKER = "AAAAAAAAAA";
    private static final String UPDATED_LOCKER = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .productStatus(DEFAULT_PRODUCT_STATUS)
            .active(DEFAULT_ACTIVE)
            .quantityInStock(DEFAULT_QUANTITY_IN_STOCK)
            .nbrOfSells(DEFAULT_NBR_OF_SELLS)
            .minimalQuantity(DEFAULT_MINIMAL_QUANTITY)
            .maximalQuantity(DEFAULT_MAXIMAL_QUANTITY)
            .weightedAveragePrice(DEFAULT_WEIGHTED_AVERAGE_PRICE)
            .location(DEFAULT_LOCATION)
            .consumptionDeadline(DEFAULT_CONSUMPTION_DEADLINE)
            .barCode(DEFAULT_BAR_CODE)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .section(DEFAULT_SECTION)
            .hallway(DEFAULT_HALLWAY)
            .productDisplay(DEFAULT_PRODUCT_DISPLAY)
            .locker(DEFAULT_LOCKER)
            .productCode(DEFAULT_PRODUCT_CODE)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updated(DEFAULT_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productStatus(UPDATED_PRODUCT_STATUS)
            .active(UPDATED_ACTIVE)
            .quantityInStock(UPDATED_QUANTITY_IN_STOCK)
            .nbrOfSells(UPDATED_NBR_OF_SELLS)
            .minimalQuantity(UPDATED_MINIMAL_QUANTITY)
            .maximalQuantity(UPDATED_MAXIMAL_QUANTITY)
            .weightedAveragePrice(UPDATED_WEIGHTED_AVERAGE_PRICE)
            .location(UPDATED_LOCATION)
            .consumptionDeadline(UPDATED_CONSUMPTION_DEADLINE)
            .barCode(UPDATED_BAR_CODE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .section(UPDATED_SECTION)
            .hallway(UPDATED_HALLWAY)
            .productDisplay(UPDATED_PRODUCT_DISPLAY)
            .locker(UPDATED_LOCKER)
            .productCode(UPDATED_PRODUCT_CODE)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);
        assertThat(testProduct.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProduct.getQuantityInStock()).isEqualTo(DEFAULT_QUANTITY_IN_STOCK);
        assertThat(testProduct.getNbrOfSells()).isEqualTo(DEFAULT_NBR_OF_SELLS);
        assertThat(testProduct.getMinimalQuantity()).isEqualTo(DEFAULT_MINIMAL_QUANTITY);
        assertThat(testProduct.getMaximalQuantity()).isEqualTo(DEFAULT_MAXIMAL_QUANTITY);
        assertThat(testProduct.getWeightedAveragePrice()).isEqualByComparingTo(DEFAULT_WEIGHTED_AVERAGE_PRICE);
        assertThat(testProduct.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testProduct.getConsumptionDeadline()).isEqualTo(DEFAULT_CONSUMPTION_DEADLINE);
        assertThat(testProduct.getBarCode()).isEqualTo(DEFAULT_BAR_CODE);
        assertThat(testProduct.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testProduct.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testProduct.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testProduct.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testProduct.getHallway()).isEqualTo(DEFAULT_HALLWAY);
        assertThat(testProduct.getProductDisplay()).isEqualTo(DEFAULT_PRODUCT_DISPLAY);
        assertThat(testProduct.getLocker()).isEqualTo(DEFAULT_LOCKER);
        assertThat(testProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProduct.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testProduct.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setActive(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityInStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setQuantityInStock(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productStatus").value(hasItem(DEFAULT_PRODUCT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityInStock").value(hasItem(DEFAULT_QUANTITY_IN_STOCK.doubleValue())))
            .andExpect(jsonPath("$.[*].nbrOfSells").value(hasItem(DEFAULT_NBR_OF_SELLS)))
            .andExpect(jsonPath("$.[*].minimalQuantity").value(hasItem(DEFAULT_MINIMAL_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].maximalQuantity").value(hasItem(DEFAULT_MAXIMAL_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].weightedAveragePrice").value(hasItem(sameNumber(DEFAULT_WEIGHTED_AVERAGE_PRICE))))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].consumptionDeadline").value(hasItem(DEFAULT_CONSUMPTION_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].hallway").value(hasItem(DEFAULT_HALLWAY)))
            .andExpect(jsonPath("$.[*].productDisplay").value(hasItem(DEFAULT_PRODUCT_DISPLAY)))
            .andExpect(jsonPath("$.[*].locker").value(hasItem(DEFAULT_LOCKER)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(sameInstant(DEFAULT_UPDATED))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.productStatus").value(DEFAULT_PRODUCT_STATUS.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.quantityInStock").value(DEFAULT_QUANTITY_IN_STOCK.doubleValue()))
            .andExpect(jsonPath("$.nbrOfSells").value(DEFAULT_NBR_OF_SELLS))
            .andExpect(jsonPath("$.minimalQuantity").value(DEFAULT_MINIMAL_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.maximalQuantity").value(DEFAULT_MAXIMAL_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.weightedAveragePrice").value(sameNumber(DEFAULT_WEIGHTED_AVERAGE_PRICE)))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.consumptionDeadline").value(DEFAULT_CONSUMPTION_DEADLINE.toString()))
            .andExpect(jsonPath("$.barCode").value(DEFAULT_BAR_CODE))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.hallway").value(DEFAULT_HALLWAY))
            .andExpect(jsonPath("$.productDisplay").value(DEFAULT_PRODUCT_DISPLAY))
            .andExpect(jsonPath("$.locker").value(DEFAULT_LOCKER))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updated").value(sameInstant(DEFAULT_UPDATED)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productStatus(UPDATED_PRODUCT_STATUS)
            .active(UPDATED_ACTIVE)
            .quantityInStock(UPDATED_QUANTITY_IN_STOCK)
            .nbrOfSells(UPDATED_NBR_OF_SELLS)
            .minimalQuantity(UPDATED_MINIMAL_QUANTITY)
            .maximalQuantity(UPDATED_MAXIMAL_QUANTITY)
            .weightedAveragePrice(UPDATED_WEIGHTED_AVERAGE_PRICE)
            .location(UPDATED_LOCATION)
            .consumptionDeadline(UPDATED_CONSUMPTION_DEADLINE)
            .barCode(UPDATED_BAR_CODE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .section(UPDATED_SECTION)
            .hallway(UPDATED_HALLWAY)
            .productDisplay(UPDATED_PRODUCT_DISPLAY)
            .locker(UPDATED_LOCKER)
            .productCode(UPDATED_PRODUCT_CODE)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProduct.getQuantityInStock()).isEqualTo(UPDATED_QUANTITY_IN_STOCK);
        assertThat(testProduct.getNbrOfSells()).isEqualTo(UPDATED_NBR_OF_SELLS);
        assertThat(testProduct.getMinimalQuantity()).isEqualTo(UPDATED_MINIMAL_QUANTITY);
        assertThat(testProduct.getMaximalQuantity()).isEqualTo(UPDATED_MAXIMAL_QUANTITY);
        assertThat(testProduct.getWeightedAveragePrice()).isEqualByComparingTo(UPDATED_WEIGHTED_AVERAGE_PRICE);
        assertThat(testProduct.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProduct.getConsumptionDeadline()).isEqualTo(UPDATED_CONSUMPTION_DEADLINE);
        assertThat(testProduct.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testProduct.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testProduct.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testProduct.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testProduct.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testProduct.getHallway()).isEqualTo(UPDATED_HALLWAY);
        assertThat(testProduct.getProductDisplay()).isEqualTo(UPDATED_PRODUCT_DISPLAY);
        assertThat(testProduct.getLocker()).isEqualTo(UPDATED_LOCKER);
        assertThat(testProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .active(UPDATED_ACTIVE)
            .quantityInStock(UPDATED_QUANTITY_IN_STOCK)
            .location(UPDATED_LOCATION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .hallway(UPDATED_HALLWAY)
            .productDisplay(UPDATED_PRODUCT_DISPLAY)
            .locker(UPDATED_LOCKER)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getProductStatus()).isEqualTo(DEFAULT_PRODUCT_STATUS);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProduct.getQuantityInStock()).isEqualTo(UPDATED_QUANTITY_IN_STOCK);
        assertThat(testProduct.getNbrOfSells()).isEqualTo(DEFAULT_NBR_OF_SELLS);
        assertThat(testProduct.getMinimalQuantity()).isEqualTo(DEFAULT_MINIMAL_QUANTITY);
        assertThat(testProduct.getMaximalQuantity()).isEqualTo(DEFAULT_MAXIMAL_QUANTITY);
        assertThat(testProduct.getWeightedAveragePrice()).isEqualByComparingTo(DEFAULT_WEIGHTED_AVERAGE_PRICE);
        assertThat(testProduct.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProduct.getConsumptionDeadline()).isEqualTo(DEFAULT_CONSUMPTION_DEADLINE);
        assertThat(testProduct.getBarCode()).isEqualTo(DEFAULT_BAR_CODE);
        assertThat(testProduct.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testProduct.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testProduct.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testProduct.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testProduct.getHallway()).isEqualTo(UPDATED_HALLWAY);
        assertThat(testProduct.getProductDisplay()).isEqualTo(UPDATED_PRODUCT_DISPLAY);
        assertThat(testProduct.getLocker()).isEqualTo(UPDATED_LOCKER);
        assertThat(testProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testProduct.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productStatus(UPDATED_PRODUCT_STATUS)
            .active(UPDATED_ACTIVE)
            .quantityInStock(UPDATED_QUANTITY_IN_STOCK)
            .nbrOfSells(UPDATED_NBR_OF_SELLS)
            .minimalQuantity(UPDATED_MINIMAL_QUANTITY)
            .maximalQuantity(UPDATED_MAXIMAL_QUANTITY)
            .weightedAveragePrice(UPDATED_WEIGHTED_AVERAGE_PRICE)
            .location(UPDATED_LOCATION)
            .consumptionDeadline(UPDATED_CONSUMPTION_DEADLINE)
            .barCode(UPDATED_BAR_CODE)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .section(UPDATED_SECTION)
            .hallway(UPDATED_HALLWAY)
            .productDisplay(UPDATED_PRODUCT_DISPLAY)
            .locker(UPDATED_LOCKER)
            .productCode(UPDATED_PRODUCT_CODE)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .updated(UPDATED_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getProductStatus()).isEqualTo(UPDATED_PRODUCT_STATUS);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProduct.getQuantityInStock()).isEqualTo(UPDATED_QUANTITY_IN_STOCK);
        assertThat(testProduct.getNbrOfSells()).isEqualTo(UPDATED_NBR_OF_SELLS);
        assertThat(testProduct.getMinimalQuantity()).isEqualTo(UPDATED_MINIMAL_QUANTITY);
        assertThat(testProduct.getMaximalQuantity()).isEqualTo(UPDATED_MAXIMAL_QUANTITY);
        assertThat(testProduct.getWeightedAveragePrice()).isEqualByComparingTo(UPDATED_WEIGHTED_AVERAGE_PRICE);
        assertThat(testProduct.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProduct.getConsumptionDeadline()).isEqualTo(UPDATED_CONSUMPTION_DEADLINE);
        assertThat(testProduct.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testProduct.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testProduct.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testProduct.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testProduct.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testProduct.getHallway()).isEqualTo(UPDATED_HALLWAY);
        assertThat(testProduct.getProductDisplay()).isEqualTo(UPDATED_PRODUCT_DISPLAY);
        assertThat(testProduct.getLocker()).isEqualTo(UPDATED_LOCKER);
        assertThat(testProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProduct.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
