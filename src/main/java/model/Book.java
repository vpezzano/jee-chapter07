package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.persistence.GeneratedValue;

/*
 * In MySQL, the table structure can be seen as follows:
 * SHOW CREATE TABLE <tablename>.
 */
@Entity
@NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM model.Book b")
public class Book implements Serializable {
	public static final String FIND_ALL = "Book.findAll";
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue()
	private Long id;
	// An entity is automatically enabled for optimistic locking
	// if it has a property mapped with a @Version annotation
	@Version
	private Integer version;
	private String title;
	private Float price;
	private String description;
	private String isbn;
	private Integer nbOfPage;
	private Boolean illustrations;

	public Book() {
	}

	public Book(String title, Float price, String description, String isbn, Integer nbOfPage, Boolean illustrations) {
		super();
		this.title = title;
		this.price = price;
		this.description = description;
		this.isbn = isbn;
		this.nbOfPage = nbOfPage;
		this.illustrations = illustrations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getNbOfPage() {
		return nbOfPage;
	}

	public void setNbOfPage(Integer nbOfPage) {
		this.nbOfPage = nbOfPage;
	}

	public Boolean getIllustrations() {
		return illustrations;
	}

	public void setIllustrations(Boolean illustrations) {
		this.illustrations = illustrations;
	}

	public void increasePrice(float increase) {
		this.price += increase;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", version=" + version + ", title=" + title + ", price=" + price + ", description="
				+ description + ", isbn=" + isbn + ", nbOfPage=" + nbOfPage + ", illustrations=" + illustrations + "]";
	}
}