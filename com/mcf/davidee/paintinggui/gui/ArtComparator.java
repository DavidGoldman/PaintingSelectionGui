package com.mcf.davidee.paintinggui.gui;

import java.util.Comparator;

import net.minecraft.util.EnumArt;

public class ArtComparator implements Comparator<EnumArt> {
	
	@Override
	public int compare(EnumArt a, EnumArt b) {
		int xA = a.sizeX, yA = a.sizeY;
		int xB = b.sizeX, yB = b.sizeY;
		if (xB*yB > xA*yA)
			return 1;
		if (xB*yB == xA * yA){
			if (xB == xA)
				return 0;
			if (yB > yA)
				return 1;
		}
		return -1;
	}
}

