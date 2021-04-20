
package capstone.techmatrix.beacondetector.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ranking {

    @SerializedName("ranking")
    @Expose
    private String ranking;
    @SerializedName("products")
    @Expose
    private List<ProductRank> products = null;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public List<ProductRank> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRank> products) {
        this.products = products;
    }

}
