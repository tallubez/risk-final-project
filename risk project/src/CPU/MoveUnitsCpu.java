package CPU;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import RunGame.MoveUnits;
import board.Territory;
import main.Player;
import main.UI;

public class MoveUnitsCpu {

	public MoveUnitsCpu() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * manage movement after conquering for ai player
	 * 
	 * @param ui
	 * @param org  origin territory
	 * @param dest destination territory
	 * @return how much to move;
	 */
	public static double MoveAfterConq(UI ui, Territory org, Territory dest) {
		double orgOptimizeUnits, destOptimizeUnits;
		double maxMovement = org.getUnitAmount() - 1;
		double totalUnits = org.getUnitAmount() + dest.getUnitAmount();
		if (maxMovement == 0) {
			return 0;
		}
		double orgValue = PositionTroops.CalcTerritoryImportanceDefensive(ui, ui.getPlayer(2), org, null);
		if (orgValue == 0) {
			return maxMovement;
		}
		double destValue = PositionTroops.CalcTerritoryImportanceDefensive(ui, ui.getPlayer(2), dest, null);
		if (destValue == 0) {
			return 0;
		}
		double totalvalue = orgValue + destValue;
		orgValue = orgValue / (totalvalue);
		destValue = destValue / (totalvalue);
		orgOptimizeUnits = Math.floor(orgValue * totalUnits);
		destOptimizeUnits = totalUnits - orgOptimizeUnits;
		if (destOptimizeUnits <= dest.getUnitAmount()) {
			return 0;
		}
		double neededMovemnet = destOptimizeUnits - dest.getUnitAmount();
		return Math.min(maxMovement, neededMovemnet);
	}

	/**
	 * select from where and how much to move at the end of turn
	 * 
	 * @param ui
	 * @param cpu
	 */
	public static void moveEndOfTurn(UI ui, Player cpu) {
		Territory dest = null, org = null;
		double maxMovement = 0, temp;
		ArrayList<Territory> possibilities = new ArrayList<>();
		for (Territory t : cpu.territories_controling) {
			possibilities.clear();
			MoveUnits.GetTerritorysConected(ui, cpu, t, possibilities);
			for (Territory t_n : possibilities) {
				temp = MoveAfterConq(ui, t, t_n);
				if (temp > maxMovement) {
					maxMovement = temp;
					org = t;
					dest = t_n;
				}

			}
		}
		if (maxMovement > 0) {
			JOptionPane.showMessageDialog(null,
					"computer moved " + maxMovement + " units from " + org.getName() + " to " + dest.getName());
			org.subUnits(maxMovement);
			dest.addUnits(maxMovement);
			cpu.getTerritoryList().updateText(dest);
			cpu.getTerritoryList().updateText(org);
		} else {
			JOptionPane.showMessageDialog(null, "computer didn't move units");
		}

	}

}
