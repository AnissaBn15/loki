package com.loki.service.dto;

import com.loki.domain.enumeration.ProductStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.loki.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    private String reference;

    private String name;

    @Size(max = 3000)
    private String description;

    private ProductStatus productStatus;

    @NotNull
    private Boolean active;

    @NotNull
    private Double quantityInStock;

    private Integer nbrOfSells;

    private String imagePath;

    @DecimalMin(value = "0")
    private Double minimalQuantity;

    @DecimalMin(value = "0")
    private Double maximalQuantity;

    @DecimalMin(value = "0")
    private BigDecimal weightedAveragePrice;

    private String location;

    private LocalDate consumptionDeadline;

    private String barCode;

    private String serialNumber;

    private String brand;

    private String model;

    private String section;

    private String hallway;

    private String productDisplay;

    private String locker;

    private String productCode;

    private ZonedDateTime created;

    private String createdBy;

    private ZonedDateTime updated;

    private String updatedBy;

    private FournisseurDTO fournisseur;

    private ProductCategoryDTO productCategory;

    private Long productCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getNbrOfSells() {
        return nbrOfSells;
    }

    public void setNbrOfSells(Integer nbrOfSells) {
        this.nbrOfSells = nbrOfSells;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getMinimalQuantity() {
        return minimalQuantity;
    }

    public void setMinimalQuantity(Double minimalQuantity) {
        this.minimalQuantity = minimalQuantity;
    }

    public Double getMaximalQuantity() {
        return maximalQuantity;
    }

    public void setMaximalQuantity(Double maximalQuantity) {
        this.maximalQuantity = maximalQuantity;
    }

    public BigDecimal getWeightedAveragePrice() {
        return weightedAveragePrice;
    }

    public void setWeightedAveragePrice(BigDecimal weightedAveragePrice) {
        this.weightedAveragePrice = weightedAveragePrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getConsumptionDeadline() {
        return consumptionDeadline;
    }

    public void setConsumptionDeadline(LocalDate consumptionDeadline) {
        this.consumptionDeadline = consumptionDeadline;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getHallway() {
        return hallway;
    }

    public void setHallway(String hallway) {
        this.hallway = hallway;
    }

    public String getProductDisplay() {
        return productDisplay;
    }

    public void setProductDisplay(String productDisplay) {
        this.productDisplay = productDisplay;
    }

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public ProductCategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryDTO productCategory) {
        this.productCategory = productCategory;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
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
            ", fournisseur=" + getFournisseur() +
            ", productCategory=" + getProductCategory() +
            "}";
    }
}
