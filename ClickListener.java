import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClickListener implements ActionListener {

    private Button b;
    private String input;
    
    public ClickListener(Button _b) {
        _b.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        input = b.getActionCommand();
        
    }
    
    public String returnAnswer() {
        return input;
    }
    
}
