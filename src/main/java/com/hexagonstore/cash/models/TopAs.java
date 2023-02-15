package com.hexagonstore.cash.models;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import lombok.Data;
import org.bukkit.entity.ArmorStand;

@Data
public class TopAs {

    private final int pos;
    private final ArmorStand as;
    private Hologram hologram;

    public void clearLines() {
        if(hologram != null) hologram.clearLines();
    }

}
