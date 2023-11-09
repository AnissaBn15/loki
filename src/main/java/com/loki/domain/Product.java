package com.loki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loki.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "name")
    private String name;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "quantity_in_stock", nullable = false)
    private Double quantityInStock;

    @Column(name = "nbr_of_sells")
    private Integer nbrOfSells;

    @Column(name = "image_path")
    private String imagePath;

    @DecimalMin(value = "0")
    @Column(name = "minimal_quantity")
    private Double minimalQuantity;

    @DecimalMin(value = "0")
    @Column(name = "maximal_quantity")
    private Double maximalQuantity;

    @DecimalMin(value = "0")
    @Column(name = "weighted_average_price", precision = 21, scale = 2)
    private BigDecimal weightedAveragePrice;

    @Column(name = "location")
    private String location;

    @Column(name = "consumption_deadline")
    private LocalDate consumptionDeadline;

    @Column(name = "bar_code")
    private String barCode;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "section")
    private String section;

    @Column(name = "hallway")
    private String hallway;

    @Column(name = "product_display")
    private String productDisplay;

    @Column(name = "locker")
    private String locker;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commands", "paiements", "products" }, allowSetters = true)
    private Fournisseur fournisseur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product", "client", "panier", "command" }, allowSetters = true)
    private Set<LineOfCommand> linesCommands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Product reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getProductStatus() {
        return this.productStatus;
    }

    public Product productStatus(ProductStatus productStatus) {
        this.setProductStatus(productStatus);
        return this;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Product active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getQuantityInStock() {
        return this.quantityInStock;
    }

    public Product quantityInStock(Double quantityInStock) {
        this.setQuantityInStock(quantityInStock);
        return this;
    }

    public void setQuantityInStock(Double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getNbrOfSells() {
        return this.nbrOfSells;
    }

    public Product nbrOfSells(Integer nbrOfSells) {
        this.setNbrOfSells(nbrOfSells);
        return this;
    }

    public void setNbrOfSells(Integer nbrOfSells) {
        this.nbrOfSells = nbrOfSells;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Product imagePath(String imagePath) {
        this.setImagePath(imagePath);
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getMinimalQuantity() {
        return this.minimalQuantity;
    }

    public Product minimalQuantity(Double minimalQuantity) {
        this.setMinimalQuantity(minimalQuantity);
        return this;
    }

    public void setMinimalQuantity(Double minimalQuantity) {
        this.minimalQuantity = minimalQuantity;
    }

    public Double getMaximalQuantity() {
        return this.maximalQuantity;
    }

    public Product maximalQuantity(Double maximalQuantity) {
        this.setMaximalQuantity(maximalQuantity);
        return this;
    }

    public void setMaximalQuantity(Double maximalQuantity) {
        this.maximalQuantity = maximalQuantity;
    }

    public BigDecimal getWeightedAveragePrice() {
        return this.weightedAveragePrice;
    }

    public Product weightedAveragePrice(BigDecimal weightedAveragePrice) {
        this.setWeightedAveragePrice(weightedAveragePrice);
        return this;
    }

    public void setWeightedAveragePrice(BigDecimal weightedAveragePrice) {
        this.weightedAveragePrice = weightedAveragePrice;
    }

    public String getLocation() {
        return this.location;
    }

    public Product location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getConsumptionDeadline() {
        return this.consumptionDeadline;
    }

    public Product consumptionDeadline(LocalDate consumptionDeadline) {
        this.setConsumptionDeadline(consumptionDeadline);
        return this;
    }

    public void setConsumptionDeadline(LocalDate consumptionDeadline) {
        this.consumptionDeadline = consumptionDeadline;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public Product barCode(String barCode) {
        this.setBarCode(barCode);
        return this;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Product serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return this.brand;
    }

    public Product brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Product model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSection() {
        return this.section;
    }

    public Product section(String section) {
        this.setSection(section);
        return this;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getHallway() {
        return this.hallway;
    }

    public Product hallway(String hallway) {
        this.setHallway(hallway);
        return this;
    }

    public void setHallway(String hallway) {
        this.hallway = hallway;
    }

    public String getProductDisplay() {
        return this.productDisplay;
    }

    public Product productDisplay(String productDisplay) {
        this.setProductDisplay(productDisplay);
        return this;
    }

    public void setProductDisplay(String productDisplay) {
        this.productDisplay = productDisplay;
    }

    public String getLocker() {
        return this.locker;
    }

    public Product locker(String locker) {
        this.setLocker(locker);
        return this;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Product productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Product created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Product updated(ZonedDateTime updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Product updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Product fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public ProductCategory getProductCategory() {
        return this.productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Product productCategory(ProductCategory productCategory) {
        this.setProductCategory(productCategory);
        return this;
    }

    public Set<LineOfCommand> getLinesCommands() {
        return this.linesCommands;
    }

    public void setLinesCommands(Set<LineOfCommand> lineOfCommands) {
        if (this.linesCommands != null) {
            this.linesCommands.forEach(i -> i.setProduct(null));
        }
        if (lineOfCommands != null) {
            lineOfCommands.forEach(i -> i.setProduct(this));
        }
        this.linesCommands = lineOfCommands;
    }

    public Product linesCommands(Set<LineOfCommand> lineOfCommands) {
        this.setLinesCommands(lineOfCommands);
        return this;
    }

    public Product addLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.add(lineOfCommand);
        lineOfCommand.setProduct(this);
        return this;
    }

    public Product removeLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.remove(lineOfCommand);
        lineOfCommand.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productStatus='" + getProductStatus() + "'" +
            ", active='" + getActive() + "'" +
            ", quantityInStock=" + getQuantityInStock() +
            ", nbrOfSells=" + getNbrOfSells() +
            ", imagePath='" + getImagePath() + "'" +
            ", minimalQuantity=" + getMinimalQuantity() +
            ", maximalQuantity=" + getMaximalQuantity() +
            ", weightedAveragePrice=" + getWeightedAveragePrice() +
            ", location='" + getLocation() + "'" +
            ", consumptionDeadline='" + getConsumptionDeadline() + "'" +
            ", barCode='" + getBarCode() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", section='" + getSection() + "'" +
            ", hallway='" + getHallway() + "'" +
            ", productDisplay='" + getProductDisplay() + "'" +
            ", locker='" + getLocker() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
