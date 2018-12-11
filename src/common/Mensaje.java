package common;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Mensaje {
    public static void print(String nombreMensaje,String mensaje)
    {
    	JFrame err=new JFrame(nombreMensaje);
        Container panel = err.getContentPane();
        panel.setLayout(null);
        err.setSize(400,200);
        JLabel mensajeError= new JLabel(mensaje);
        mensajeError.setBounds(20,20,380,30);
        mensajeError.setVisible(true);
        panel.add(mensajeError);
        JButton ok=new JButton("OK");
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {err.setVisible(false);}
        });
        ok.setBounds(160,100,80,40);
        panel.add(ok);
        ok.setVisible(true);
        err.setVisible(true);
    }
}
