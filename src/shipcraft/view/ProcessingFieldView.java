package shipcraft.view;

/**
 * @version $Id: ProcessingFieldView.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng, stream
 */
public class ProcessingFieldView extends AbstractFieldView {
    private PPApplet ppApplet = new PPApplet();

    public ProcessingFieldView(){
        super();
        add(ppApplet);
        ppApplet.init();
    }

    public void update() {
        ppApplet.redraw();
    }
}