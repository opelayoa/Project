package com.tiendas3b.almacen.receipt.bulk;

import com.tiendas3b.almacen.db.dao.EqualsBase;
import com.tiendas3b.almacen.db.dao.VArticle;

/**
 * Created by danflo on 13/02/2018 madafaka.
 */

public class VArticleInBulk extends EqualsBase {

    private VArticle article;
    private int total;
    private int count;

    public VArticle getArticle() {
        return article;
    }

    public void setArticle(VArticle article) {
        this.article = article;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public Long getId() {
        return article.getId();
    }
}
