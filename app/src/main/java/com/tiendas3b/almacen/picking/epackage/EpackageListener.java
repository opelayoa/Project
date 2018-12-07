package com.tiendas3b.almacen.picking.epackage;

import com.tiendas3b.almacen.db.dao.EPackage;

import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by danflo on 06/06/2017 madafaka.
 */

public interface EpackageListener {
    void onClick(EPackage ePackage, FabButton btn);
}
