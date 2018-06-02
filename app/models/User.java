package models;

import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * User entity managed by Ebean
 */
@Entity
@Table(name="users")
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String role;

}