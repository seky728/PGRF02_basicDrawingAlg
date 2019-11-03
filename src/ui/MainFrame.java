package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Renderer.Renderer;
import drawableObjects.Enum.DRAWABLEOBJECTS;
import drawableObjects.Objects.*;
import drawableObjects.Objects.Point;
import filler.SeedFiller;

public class MainFrame extends JFrame implements MouseMotionListener{
    private BufferedImage img;
    private JPanel drawPanel;
    private JPanel radiosPanel;

    private Renderer renderer;
    private List<Drawable> drawablesList;
    private JRadioButton rbLine, rbIregularPolygon, rbRegularPolygon, rbFillBySeed;
    private JButton clearBtn;
    private ButtonGroup btnTopGroup;
    private Boolean dragged = Boolean.FALSE;
    private DRAWABLEOBJECTS type = DRAWABLEOBJECTS.LINE;  //default choice
    private int clickX, clickY, draggedX, draggedY;
    private int mouseClicked = 0;
    private Boolean clicked;
    private Boolean released;
    private int mouseX, mouseY;
    private List<Point> iregularPolygonPoits = new ArrayList<>();
    private Boolean iregularPolygonCreated;
    private Boolean changeEdge = false;
    private RegularPolygon regularPolygon;
    private SeedFiller seedFiller;
    private List<Point> coloredPixels = new ArrayList<>();


    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FPS = 50;


    public MainFrame(){
        this.img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        setFrame();
    }

    private void setFrame(){


        renderer = new Renderer(img);

        drawablesList = new ArrayList<>();

        seedFiller = new SeedFiller(img, coloredPixels);

        seedFiller.setNewColor(Color.red);

        //set up frame
        setLayout(new BorderLayout());
        setTitle("Secký - sem. proj 1");
        setSize(WIDTH,HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //create radio and draw panel
        radiosPanel = new JPanel();
        drawPanel = new JPanel();

        //add radio and draw to frame
        add(radiosPanel, BorderLayout.NORTH); //add JPanel with radios selection on top
        add(drawPanel, BorderLayout.CENTER);


        setUpTopLine();
        addMouseEvents();



        setVisible(true);


        //start rendering
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 1000, FPS);

    }

    private void draw(){

            img.getGraphics().fillRect(0,0,img.getWidth(),img.getHeight());

            realTimeRenderer();

            for (Drawable drawable : drawablesList) {
                drawable.draw(renderer);
            }
            if (regularPolygon != null){
                regularPolygon.draw(renderer);
            }

            if (seedFiller.isCanDraw()){
                for (Point point : coloredPixels){
                    point.draw(renderer);
                }
            } else {
                System.out.println("I am loading field with coloredPixels");
            }

            drawPanel.getGraphics().drawImage(img,0,0,null);
            drawPanel.paintComponents(getGraphics());





    }

    private void realTimeRenderer(){
        if (changeEdge){
            Point corner = new Point(mouseX,clickY);
            regularPolygon.changeEdge(new Point(clickX, clickY),corner, corner, new Point(mouseX, mouseY));
        }

        if (dragged){
            Color r = Color.blue;
            switch (type){
                case LINE:
                case POLYGON:
                    renderer.useDDA(clickX, clickY, draggedX, draggedY, r);
                    break;
                case N_OBJECT:
                    int choice = iregularPolygonPoits.size();
                    if (choice == 1){
                        renderer.useDDA(iregularPolygonPoits.get(0).getX(),iregularPolygonPoits.get(0).getY(),draggedX,draggedY,r);
                    } else {
                        renderer.useDDA(iregularPolygonPoits.get(0).getX(),iregularPolygonPoits.get(0).getY(),draggedX,draggedY,r);
                        renderer.useDDA(iregularPolygonPoits.get(choice-1).getX(),iregularPolygonPoits.get(choice-1).getY(),draggedX,draggedY,r);

                    }
                    break;
            }
        }
        if (mouseClicked != 0 && type == DRAWABLEOBJECTS.POLYGON){
            preDraw();
        }
    }

    private void preDraw(){
        if (dragged) {
            mouseX = draggedX;
            mouseY = draggedY;
        }
        switch (mouseClicked) {
            case 1:
                regularPolygon.addPoint(new Point(clickX, clickY), new Point(mouseX, mouseY));
                renderer.useDDA(clickX, clickY, mouseX, mouseY, regularPolygon.getColor());
                break;
            case 2:
                Point rPoint = regularPolygon.getB();
                renderer.useDDA(clickX, clickY, rPoint.getX(), rPoint.getY(), regularPolygon.getColor());
                break;
        }

    }


    private void setUpTopLine(){
        clearBtn = new JButton("Clear");


        rbLine = new JRadioButton("Lines");
        rbLine.setToolTipText("Radio box for Line");

        rbIregularPolygon = new JRadioButton("Iregular polygon");
        rbIregularPolygon.setToolTipText("Radio box for Iregular polygon");

        rbRegularPolygon = new JRadioButton("Regular polygon");
        rbRegularPolygon.setToolTipText("Radio box for Regular polygon");

        rbFillBySeed = new JRadioButton("Fill by seed");
        rbFillBySeed.setToolTipText("Fill by seed algorithm");

        setRadioButtonListener();

        //radio groupa, protože může být zvolen jen jeden vykreslující se obrazec
        btnTopGroup = new ButtonGroup();
        btnTopGroup.add(rbLine);
        btnTopGroup.add(rbIregularPolygon);
        btnTopGroup.add(rbRegularPolygon);
        btnTopGroup.add(rbFillBySeed);

        rbLine.setSelected(true); // set default

        //vložení radio btn do topPanelu
        radiosPanel.add(rbLine);
        radiosPanel.add(rbIregularPolygon);
        radiosPanel.add(rbRegularPolygon);
        radiosPanel.add(rbFillBySeed);
        radiosPanel.add(clearBtn);


    }

    private void setRadioButtonListener(){
        rbLine.addActionListener((e)->{
            mouseClicked = 0;
            iregularPolygonCreated = false;
            clicked = false;
            dragged = false;
            changeEdge = false;
            changeTypeByRadioButton(DRAWABLEOBJECTS.LINE);
        });

        rbIregularPolygon.addActionListener((e)->{
            mouseClicked = 0;
            iregularPolygonPoits.clear();
            iregularPolygonCreated = false;
            changeEdge = false;
            changeTypeByRadioButton(DRAWABLEOBJECTS.N_OBJECT);
        });

        rbRegularPolygon.addActionListener((e)->{
            regularPolygon = new RegularPolygon(Color.red);
            mouseClicked = 0;
            iregularPolygonCreated = false;
            clicked = false;
            dragged = false;
            changeEdge = false;
            changeTypeByRadioButton(DRAWABLEOBJECTS.POLYGON);
        });

        rbFillBySeed.addActionListener((e)->{
            mouseClicked = 0;
            iregularPolygonCreated = false;
            clicked = false;
            dragged = false;
            changeEdge = false;
            changeTypeByRadioButton(DRAWABLEOBJECTS.FILL_SEED);
        });


        clearBtn.addActionListener((e)->{
            mouseClicked = 0;
            iregularPolygonPoits.clear();
            dragged = false;
            clicked = false;
            drawablesList.clear();
            changeEdge = false;
            regularPolygon = null;

        });




    }
    private void changeTypeByRadioButton(DRAWABLEOBJECTS type){
        this.type = type;
    }

    private void addMouseEvents() {
        drawPanel.addMouseMotionListener(this); // odkazuji se implementovaté metody mouseDragged a mouseMoved
        drawPanel.addMouseListener(new MouseAdapter() {
            int x2 , y2;
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (type){
                    case LINE:
                        x2 = e.getX();
                        y2 = e.getY();
                        drawablesList.add(new Line(new Point(clickX,clickY), new Point(x2,y2)));
                        mouseClicked = 0;
                        clicked = false;
                        dragged = false;
                        released = true;
                        break;


                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

                switch (type){
                    case LINE:
                        if (mouseClicked == 0){
                            clickX = e.getX();
                            clickY = e.getY();
                            clicked = true;
                            mouseClicked++;
                            dragged = true;
                            released = false;
                        }
                        break;
                    case N_OBJECT:
                            clicked = true;
                            mouseClicked++;
                            dragged = true;
                            iregularPolygonPoits.add(new Point(e.getX(),e.getY()));

                            if (iregularPolygonCreated){

                                drawablesList.remove(drawablesList.size()-1);
                                drawablesList.add(new IrregularPolygon(new ArrayList<>(iregularPolygonPoits)));


                            } else {
                                drawablesList.add(new IrregularPolygon(iregularPolygonPoits));
                                iregularPolygonCreated = true;
                            }

                        break;
                    case POLYGON:
                        switch (mouseClicked){
                            case 0:
                                clickX = e.getX();
                                clickY = e.getY();
                                clicked = true;
                                mouseClicked ++;
                                dragged = true;
                                break;
                            case 1:
                                regularPolygon.addPoint(new Point(clickX, clickY), new Point(e.getX(), e.getY()));

                                changeEdge = !changeEdge;
                                clicked = true;
                                dragged = true;
                                mouseClicked++;
                                break;
                            case 2:
                                changeEdge = !changeEdge;
                                clicked = false;
                                dragged = false;
                                mouseClicked = 0;
                                drawablesList.add(new RegularPolygon(regularPolygon.getA(), regularPolygon.getB(), Color.black,regularPolygon.getEdge()));
                                break;
                        }
                        break;
                    case FILL_SEED:
                        seedFiller.setSeed(new Point(e.getX(),e.getY()));
                        seedFiller.setNewColor(Color.YELLOW);
                        seedFiller.setBorderColor(Color.RED);
                        seedFiller.Fill();
                        break;
                }


            }


        });
     }


    public void present(Graphics graphics){
        graphics.drawImage(img, 0,0 , null);
    }

    public void clear(){
        Graphics gr = img.getGraphics();
        gr.setColor(new Color(0x2f2f2f));
        gr.fillRect(0,0,img.getWidth(), img.getHeight());
    }


    //potřeba sledovat defaultně -> nee jen při stisknutí
    @Override
    public void mouseDragged(MouseEvent e) {

        if (type == DRAWABLEOBJECTS.LINE){
            if (!released && clicked){
                draggedX = e.getX();
                draggedY = e.getY();
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

        draggedX = mouseEvent.getX();
        draggedY = mouseEvent.getY();
    }
}
