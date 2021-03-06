/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2010 Didier Briel
               2014-2016 Aaron Madlon-Kay
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.gui.preferences.view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;

import org.omegat.core.matching.NearString.SORT_KEY;
import org.omegat.gui.matches.MatchesVarExpansion;
import org.omegat.gui.preferences.BasePreferencesController;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.gui.DelegatingComboBoxRenderer;

/**
 * @author Maxym Mykhalchuk
 * @author Didier Briel
 * @author Aaron Madlon-Kay
 */
public class TMMatchesPreferencesController extends BasePreferencesController {

    private TMMatchesPreferencesPanel panel;

    @Override
    public JComponent getGui() {
        if (panel == null) {
            initGui();
            initFromPrefs();
        }
        return panel;
    }

    @Override
    public String toString() {
        return OStrings.getString("PREFS_TITLE_TM_MATCHES");
    }

    private void initGui() {
        panel = new TMMatchesPreferencesPanel();
        panel.sortMatchesList.setModel(new DefaultComboBoxModel<>(
                new SORT_KEY[] { SORT_KEY.SCORE, SORT_KEY.SCORE_NO_STEM, SORT_KEY.ADJUSTED_SCORE }));
        panel.sortMatchesList.setRenderer(new DelegatingComboBoxRenderer<SORT_KEY, String>() {
            @Override
            protected String getDisplayText(SORT_KEY value) {
                return OStrings.getString("EXT_TMX_SORT_KEY_" + value.name());
            }
        });
        panel.sortMatchesList.addActionListener(e -> {
            boolean changed = valueIsDifferent(Preferences.EXT_TMX_SORT_KEY, panel.sortMatchesList.getSelectedItem());
            setReloadRequired(changed);
        });
        panel.insertButton
                .addActionListener(
                        e -> panel.matchesTemplate.replaceSelection(panel.variablesList.getSelectedItem().toString()));
    }

    @Override
    protected void initFromPrefs() {
        panel.sortMatchesList.getModel()
                .setSelectedItem(Preferences.getPreferenceEnumDefault(Preferences.EXT_TMX_SORT_KEY, SORT_KEY.SCORE));
        panel.displayLevel2Tags.setSelected(Preferences.isPreference(Preferences.EXT_TMX_SHOW_LEVEL2));
        panel.useSlash.setSelected(Preferences.isPreference(Preferences.EXT_TMX_USE_SLASH));
        panel.matchesTemplate.setText(Preferences.getPreferenceDefault(Preferences.EXT_TMX_MATCH_TEMPLATE,
                MatchesVarExpansion.DEFAULT_TEMPLATE));
        panel.matchesTemplate.setCaretPosition(0);
    }

    @Override
    public void restoreDefaults() {
        panel.sortMatchesList.getModel().setSelectedItem(SORT_KEY.SCORE);
        panel.displayLevel2Tags.setSelected(false);
        panel.useSlash.setSelected(false);
        panel.matchesTemplate.setText(MatchesVarExpansion.DEFAULT_TEMPLATE);
        panel.matchesTemplate.setCaretPosition(0);
    }

    @Override
    public void persist() {
        Preferences.setPreference(Preferences.EXT_TMX_SORT_KEY, (SORT_KEY) panel.sortMatchesList.getSelectedItem());
        Preferences.setPreference(Preferences.EXT_TMX_SHOW_LEVEL2, panel.displayLevel2Tags.isSelected());
        Preferences.setPreference(Preferences.EXT_TMX_USE_SLASH, panel.useSlash.isSelected());
        Preferences.setPreference(Preferences.EXT_TMX_MATCH_TEMPLATE, panel.matchesTemplate.getText());
    }
}
