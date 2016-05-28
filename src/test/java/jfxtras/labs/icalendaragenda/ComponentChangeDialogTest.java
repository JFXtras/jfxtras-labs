package jfxtras.labs.icalendaragenda;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.util.Pair;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper.ChangeDialogOption;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class ComponentChangeDialogTest extends JFXtrasGuiTest
{
//    private ComponentChangeDialog dialog;

    private ResourceBundle resources;
    private static final Map<ChangeDialogOption, Pair<Temporal,Temporal>> EXAMPLE_MAP = makeExampleMap();
    private static Map<ChangeDialogOption, Pair<Temporal,Temporal>> makeExampleMap()
    {
        Map<ChangeDialogOption, Pair<Temporal,Temporal>> exampleMap = new LinkedHashMap<>();
        exampleMap.put(ChangeDialogOption.ALL, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), null));
        exampleMap.put(ChangeDialogOption.ONE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), LocalDate.of(2016, 5, 25)));
        exampleMap.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 6, 25), null));
        return exampleMap;
    }
    
    @Override
    public Parent getRootNode()
    {
        resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        return new Label();
    }

    @Test
    public void canDisplayDialog()
    {
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
            EditChoiceDialog dialog = new EditChoiceDialog(EXAMPLE_MAP, resources);
            dialog.showAndWait();
        });
        Node n = find("#editChoiceDialog");
        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 600.0, 0.01);
    }

}
