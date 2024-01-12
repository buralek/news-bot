package buralek.newsbot.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue
    private BigInteger id;

    @NotNull
    private String name;

    @NotNull
    private String url;


}
