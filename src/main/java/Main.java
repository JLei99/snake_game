
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.checkerframework.checker.units.qual.C;

import javax.swing.text.StyledEditorKit;


public class Main extends Application {
    boolean quit = false;
    Scene splash, level1, level2, level3, endScene;
    enum SCENES {SPLASH, LEVEL1, LEVEL2, LEVEL3, ENDSCENE};
    //Timer timer;
    //Circle head = new Circle();
    float cir_x, cir_y;
    float cir_r = 10;
    float dx = 1.0f, dy = 1.0f;
    int direction = 0;
    int score;
    int fruit;
    int cur_score;
    int cur_fruit;
    Circle [] apples = new Circle[15];
    float [] applex = new float[15];
    float [] appley = new float[15];
    Circle circle;
    Label timerLabel = new Label();
    Timeline timeline1;
    int timeSeconds = 30;
    boolean isPaused;
    float screen_width = 1280;
    float screen_height = 800;
    Text scoreLable = new Text();
    Text fruitLable = new Text();
    Text cur_scoreLable = new Text();
    Text cur_fruitLable = new Text();
    boolean returnToSplash = false;
    Circle [] snake = new Circle[50];
    int[] cur_dir = new int[50];
    int[] next_dir = new int[50];
    int snake_len = 1;
    int cur_scene = 1; // where is the snake
    boolean eat_an_apple = false;
    Text endWords;
    Group root0;
    Group root1;
    Group root2;
    Group root3;
    int cur_level = -1;
    boolean gameEnd = false;
    AnimationTimer timer;
    int return_from = -1;
    Random rand = new Random();

    // initialize the snake of length 3
    void initialize_snake(){
        cir_x = (400 + rand.nextInt(400));
        cir_y = (250 + rand.nextInt(250));
        snake[0].setCenterX(cir_x);
        snake[0].setCenterY(cir_y);
        snake[0].setRadius(cir_r);
        snake[0].setFill(Color.DARKOLIVEGREEN);
        snake_len = 1;
        add_body();
        add_body();
    }

    void add_body(){
        int x = 0;
        int y = 0;
        if(cur_dir[snake_len - 1] == 0){
            y = 20;
        } else if(cur_dir[snake_len - 1] == 1){
            x = -20;
        } else if(cur_dir[snake_len - 1] == 2){
            y = -20;
        } else if(cur_dir[snake_len - 1] == 3){
            x = 20;
        }
        snake[snake_len].setCenterX(snake[snake_len - 1].getCenterX() + x);
        snake[snake_len].setCenterY(snake[snake_len - 1].getCenterY() + y);
        snake[snake_len].setRadius(cir_r);
        snake[snake_len].setFill(Color.DARKOLIVEGREEN);
        cur_dir[snake_len] = cur_dir[snake_len - 1];
        next_dir[snake_len] = cur_dir[snake_len];
        snake_len++;
    }


    @Override
    public void start(Stage stage)  {
        score = 0;
        isPaused = false;
        stage.setTitle("Snake game");
        //timer = new Timer();
        final int FPS = 40;
        // set up the total scoreLable and fruitLable
        timerLabel.setText("Time Left: " + timeSeconds);
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setFont(new Font(25));
        scoreLable.setText("Score: " + score);
        scoreLable.setFont(new Font(20));
        scoreLable.setX(1100);
        scoreLable.setY(50);
        fruitLable.setText("Fruits: " + fruit);
        fruitLable.setFont(new Font(20));
        fruitLable.setX(1000);
        fruitLable.setY(50);
        // set up the cur_scoreLable and cur_fruitLable
        cur_scoreLable.setText("Score in this level: " + cur_score);
        cur_scoreLable.setFont(new Font(20));
        cur_scoreLable.setX(1000);
        cur_scoreLable.setY(80);
        cur_fruitLable.setText("Fruits in this level: " + cur_fruit);
        cur_fruitLable.setFont(new Font(20));
        cur_fruitLable.setX(1000);
        cur_fruitLable.setY(110);

        cur_dir[0] = 0;
        next_dir[0] = 0;


        /*********** end scene start ******************/
        Image end_image = new Image("snake.jpg");
        ImageView end_view = new ImageView(end_image);
        end_view.setPreserveRatio(true);
        end_view.setFitWidth(1280);
        end_view.setFitHeight(800);
        endWords = new Text();
        endWords.setText("Game Over\n" +
                "Your score is: " + score );
        endWords.setFont(new Font(30));
        endWords.setX(500);
        endWords.setY(300);
        Group endNode = new Group(end_view);
        endScene = new Scene(endNode, 1280, 800, Color.LIGHTSEAGREEN);
        endNode.getChildren().add(endWords);
        /*********** end scene end ******************/


        // initialize the snake (len == 3)
        snake[0] = new Circle(10);
        snake[1] = new Circle(10);
        snake[2] = new Circle(10);
        initialize_snake();

        // initialize the timer
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!quit) {
                    // Attempt the move
                    int index = handle_animation(stage, root1, 1);
                    if (index != -1) {
                        apples[index].setCenterX(rand.nextInt(1200));
                        apples[index].setCenterY(rand.nextInt(750));
                        //setScene(stage, SCENES.LEVEL1);
                        scoreLable.setText("Score: " + score);
                        fruitLable.setText("Fruits: " + fruit);
                        cur_scoreLable.setText("Score in this level: " + cur_score);
                        cur_fruitLable.setText("Fruits in this level: " + cur_fruit);
                    }
                    // check if the snake eats an apple
                    if (eat_an_apple) {
                        // play the sound
                        String sound = getClass().getClassLoader().getResource("eat_apple.mp3").toString();
                        AudioClip clip = new AudioClip(sound);
                        clip.play();
                        snake[snake_len] = new Circle(10);
                        add_body();
                        if (cur_level == 1) {
                            root1.getChildren().add(snake[snake_len - 1]);
                        } else if (cur_level == 2) {
                            root2.getChildren().add(snake[snake_len - 1]);
                        } else if (cur_level == 3) {
                            root3.getChildren().add(snake[snake_len - 1]);
                        }
                        eat_an_apple = false;
                    }
                }
            }
        };


        /*********** level 1 start ******************/
        Image image1 = new Image("grass.jpg");
        ImageView imageView1 = new ImageView(image1);
        imageView1.setPreserveRatio(true);
        imageView1.setFitWidth(2000);
        imageView1.setFitHeight(1000);
        root1 = new Group(imageView1);
        level1 = new Scene(root1, 1280, 800, Color.DARKSEAGREEN);
        level1.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)){
                direction = (direction + 4 - 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                direction = (direction + 4 + 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.R)){ //TODO: work on how to reset
                return_from = 1;
                timeSeconds = 30;
                timeline1.stop();
                root1.getChildren().remove(timerLabel);
                timer.stop();
                returnToSplash = true;
                setScene(stage, SCENES.SPLASH);
            } else if (event.getCode().equals(KeyCode.P)){
                if(isPaused){
                    timeline1.play();
                    isPaused = false;
                } else {
                    timeline1.pause();
                    isPaused = true;
                }
            } else if (event.getCode().equals(KeyCode.Q)){
                String sound = getClass().getClassLoader().getResource("game_over.mp3").toString();
                AudioClip clip = new AudioClip(sound);
                clip.play();
                quit = true;
                timer.stop();
                timeline1.stop();
                endWords.setText("Game Over\n" +
                        "Your score is: " + score );
                setScene(stage, SCENES.ENDSCENE);
            }
        });

        /*********** level 1 end ******************/


        /*********** level 2 start ******************/
        Image image2 = new Image("grass.jpg");
        ImageView imageView2 = new ImageView(image2);
        imageView2.setPreserveRatio(true);
        imageView2.setFitWidth(2000);
        imageView2.setFitHeight(1000);
        root2 = new Group(imageView2);
        level2 = new Scene(root2, 1280, 800, Color.DARKSEAGREEN);
        level2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)){
                direction = (direction + 4 - 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                direction = (direction + 4 + 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.R)){ //TODO: work on how to reset
                timeSeconds = 30;
                return_from = 2;
                timeline1.stop();
                root2.getChildren().remove(timerLabel);
                timer.stop();
                returnToSplash = true;
                setScene(stage, SCENES.SPLASH);
            } else if (event.getCode().equals(KeyCode.P)){
                if(isPaused){
                    timeline1.play();
                    isPaused = false;
                } else {
                    timeline1.pause();
                    isPaused = true;
                }
            } else if (event.getCode().equals(KeyCode.Q)){
                String sound = getClass().getClassLoader().getResource("game_over.mp3").toString();
                AudioClip clip = new AudioClip(sound);
                clip.play();
                quit = true;
                timer.stop();
                timeline1.stop();
                endWords.setText("Game Over\n" +
                        "Your score is: " + score );
                setScene(stage, SCENES.ENDSCENE);
            }
        });
        /*********** level 2 end ******************/

        /*********** level 3 start ******************/
        Image image3 = new Image("grass.jpg");
        ImageView imageView3 = new ImageView(image3);
        imageView3.setPreserveRatio(true);
        imageView3.setFitWidth(2000);
        imageView3.setFitHeight(1000);
        root3 = new Group(imageView3);
        level3 = new Scene(root3, 1280, 800, Color.DARKSEAGREEN);
        level3.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)){
                direction = (direction + 4 - 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.RIGHT)){
                direction = (direction + 4 + 1) % 4;
                cur_dir[0] = direction;
                next_dir[0] = direction;
            } else if (event.getCode().equals(KeyCode.R)){ //TODO: work on how to reset
                timeSeconds = 30;
                return_from = 3;
                timeline1.stop();
                root3.getChildren().remove(timerLabel);
                timer.stop();
                returnToSplash = true;
                setScene(stage, SCENES.SPLASH);
            } else if (event.getCode().equals(KeyCode.P)){
                if(isPaused){
                    timeline1.play();
                    isPaused = false;
                } else {
                    timeline1.pause();
                    isPaused = true;
                }
            } else if (event.getCode().equals(KeyCode.Q)){
                String sound = getClass().getClassLoader().getResource("game_over.mp3").toString();
                AudioClip clip = new AudioClip(sound);
                clip.play();
                quit = true;
                timer.stop();
                timeline1.stop();
                endWords.setText("Game Over\n" +
                        "Your score is: " + score );
                setScene(stage, SCENES.ENDSCENE);
            }
        });
        /*********** level 3 end ******************/




        // initialize the information of apples
        applex[0] = 400;    appley[0] = 200;
        applex[1] = 900;    appley[1] = 200;
        applex[2] = 600;    appley[2] = 400;
        applex[3] = 400;    appley[3] = 600;
        applex[4] = 900;    appley[4] = 600;

        applex[5] = 500;    appley[5] = 300;
        applex[6] = 750;    appley[6] = 300;
        applex[7] = 100;    appley[7] = 500;
        applex[8] = 500;    appley[8] = 700;
        applex[9] = 750;    appley[9] = 700;

        applex[10] = 100;    appley[10] = 300;
        applex[11] = 1000;    appley[11] = 100;
        applex[12] = 1100;    appley[12] = 400;
        applex[13] = 100;    appley[13] = 500;
        applex[14] = 1000;    appley[14] = 650;

        for(int i = 0; i < 15 ;i++){
            apples[i] = new Circle(10);
            //apples[i].setFill(Color.SALMON);
            apples[i].setCenterX(applex[i]);
            apples[i].setCenterY(appley[i]);
            if(i < 5){
                apples[i].setFill(Color.LIGHTSALMON);
            } else if(i < 10){
                apples[i].setFill(Color.SALMON);
            } else{
                apples[i].setFill(Color.DARKSALMON);
            }
        }

        /*********** splash start ******************/
        //Creating a Text object
        Image image0 = new Image("snake.jpg");
        ImageView imageView0 = new ImageView(image0);
        imageView0.setPreserveRatio(true);
        imageView0.setFitWidth(1280);
        imageView0.setFitHeight(800);
        Text text = new Text();
        text.setText("Student ID: j36lei\n" +
                "Name: Jiaxin Lei\n" +
                "Welcome to the snake game\n" +
                "Press 1 to enter level 1\n" +
                "Press 2 to enter level 2\n" +
                "Press 3 to enter level 3\n" +
                "Use arrow keys(LEFT/RIGHT) to to control the snake's movements\n" +
                "Press R to return to the splash screen in level 1/2/3\n" +
                "Press P to pause the game in level 1/2/3\n" +
                "Press Q to quit the game in level 1/2/3\n" );
        text.setFont(new Font(20 ));
        text.setX(150);
        text.setY(150);
        root0 = new Group(imageView0);
        root0.getChildren().add(text);
        splash = new Scene(root0, 1280, 800, Color.DARKSEAGREEN);

        splash.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.DIGIT1)){
                initialize_snake();
                dx = 1.0f;
                dy = 1.0f;
                cur_level = 1;
                cur_fruit = 0;
                cur_score = 0;
                score = 0;
                fruit = 0;
                scoreLable.setText("Score: " + score);
                fruitLable.setText("Fruits: " + fruit);
                cur_scoreLable.setText("Score in this level: " + cur_score);
                cur_fruitLable.setText("Fruits in this level: " + cur_fruit);
                returnToSplash = false;
                isPaused = false;
                if(return_from != 1) {
                    addSnake(root1, 1);
                    addApples(root1, 1);
                    root1.getChildren().add(scoreLable);
                    root1.getChildren().add(fruitLable);
                    root1.getChildren().add(cur_scoreLable);
                    root1.getChildren().add(cur_fruitLable);
                }
                timer.start();
                countDown(stage,SCENES.LEVEL1,root1);
                setScene(stage, SCENES.LEVEL1);
            } else if (event.getCode().equals(KeyCode.DIGIT2)){
                initialize_snake();
                dx = 2.0f;
                dy = 2.0f;
                cur_level = 2;
                cur_fruit = 0;
                cur_score = 0;
                score = 0;
                fruit = 0;
                scoreLable.setText("Score: " + score);
                fruitLable.setText("Fruits: " + fruit);
                cur_scoreLable.setText("Score in this level: " + cur_score);
                cur_fruitLable.setText("Fruits in this level: " + cur_fruit);
                returnToSplash = false;
                isPaused = false;
                if(return_from != 2) {
                    addSnake(root2, 2);
                    addApples(root2, 2);
                    root2.getChildren().add(scoreLable);
                    root2.getChildren().add(fruitLable);
                    root2.getChildren().add(cur_scoreLable);
                    root2.getChildren().add(cur_fruitLable);
                }
                timer.start();
                countDown(stage,SCENES.LEVEL2,root2);
                setScene(stage, SCENES.LEVEL2);
            } else if (event.getCode().equals(KeyCode.DIGIT3)){
                dx = 4.0f;
                dy = 4.0f;
                cur_level = 3;
                returnToSplash = false;
                isPaused = false;
                cur_fruit = 0;
                cur_score = 0;
                score = 0;
                fruit = 0;
                scoreLable.setText("Score: " + score);
                fruitLable.setText("Fruits: " + fruit);
                cur_scoreLable.setText("Score in this level: " + cur_score);
                cur_fruitLable.setText("Fruits in this level: " + cur_fruit);
                if(return_from != 3) {
                    addSnake(root3, 3);
                    addApples(root3, 3);
                    root3.getChildren().add(scoreLable);
                    root3.getChildren().add(fruitLable);
                    root3.getChildren().add(cur_scoreLable);
                    root3.getChildren().add(cur_fruitLable);
                }
                timer.start();
                countDown(stage,SCENES.LEVEL3,root3);// level 3 does not have time limit
                setScene(stage, SCENES.LEVEL3);
            }

        });
        /*********** splash end ******************/

        // Add the scene to the stage and show it
        setScene(stage, SCENES.SPLASH);
        stage.show();
    }

    int handle_animation(Stage stage, Group root, int level) {

        // move the head of the snake (snake[0])
        if (!isPaused) {
            if (direction == 0) {
                cir_y -= dy;
            } else if (direction == 1) {
                cir_x += dx;
            } else if (direction == 2) {
                cir_y += dy;
            } else {
                cir_x -= dx;
            }
            snake[0].setCenterX(cir_x);
            snake[0].setCenterY(cir_y);

            // check if the snake dies(hit the wall or bites itself)
            // check if the snake hit the wall
            if(snake[0].getCenterX() < cir_r + 0.0f || snake[0].getCenterX() > screen_width - cir_r
                    ||snake[0].getCenterY() < cir_r+ 0.0f || snake[0].getCenterY() > screen_height - cir_r){
                String sound = getClass().getClassLoader().getResource("game_over.mp3").toString();
                AudioClip clip = new AudioClip(sound);
                clip.play();
                quit = true;
                timer.stop();
                timeline1.stop();
                endWords.setText("Game Over\n" +
                        "Your score is: " + score );
                setScene(stage, SCENES.ENDSCENE);
            }
            // check if the snake bites itself
            for(int i = 2; i < snake_len; i++){
                double x1 = snake[0].getCenterX();
                double y1 = snake[0].getCenterY();
                double x2 = snake[i].getCenterX();
                double y2 = snake[i].getCenterY();
                double deltaX = x1 - x2;
                double deltaY = y1 - y2;
                if ((deltaX * deltaX + deltaY * deltaY) < 4 * cir_r * cir_r) {
                    String sound = getClass().getClassLoader().getResource("game_over.mp3").toString();
                    AudioClip clip = new AudioClip(sound);
                    clip.play();
                    quit = true;
                    timer.stop();
                    timeline1.stop();
                    endWords.setText("Game Over\n" +
                            "Your score is: " + score );
                    setScene(stage, SCENES.ENDSCENE);
                }
            }

            // move the body of the snake
            for (int i = 1; i < snake_len; i++) {
                // update the cur_dir and next_dir
                if (!(snake[i].getCenterX() == snake[i - 1].getCenterX() ||
                        snake[i].getCenterY() == snake[i - 1].getCenterY())) {
                    //System.out.println("update cur_dir and next_dir");
                    next_dir[i] = cur_dir[i - 1];
                }
                // move the body according to the info of cur_dir and next_dir
                // if this snake block doesn't change the direction
                if (cur_dir[i] != next_dir[i] &&
                        (snake[i].getCenterX() == snake[i - 1].getCenterX() ||
                                snake[i].getCenterY() == snake[i - 1].getCenterY())) {
                    cur_dir[i] = next_dir[i];
                }
                if (cur_dir[i] == 0) {
                    snake[i].setCenterX(snake[i].getCenterX());
                    snake[i].setCenterY(snake[i].getCenterY() - dy);
                } else if (cur_dir[i] == 1) {
                    snake[i].setCenterX(snake[i].getCenterX() + dx);
                    snake[i].setCenterY(snake[i].getCenterY());
                } else if (cur_dir[i] == 2) {
                    snake[i].setCenterX(snake[i].getCenterX());
                    snake[i].setCenterY(snake[i].getCenterY() + dy);
                } else {
                    snake[i].setCenterX(snake[i].getCenterX() - dx);
                    snake[i].setCenterY(snake[i].getCenterY());
                }
            }
        }

        // check if the snake eats an apple
        for(int i = 0; i < 15; i++) {
            double x1 = snake[0].getCenterX();
            double y1 = snake[0].getCenterY();
            double x2 = apples[i].getCenterX();
            double y2 = apples[i].getCenterY();
            double deltaX = x1 - x2;
            double deltaY = y1 - y2;
            if ((deltaX * deltaX + deltaY * deltaY) <= 4 * cir_r * cir_r) {
                eat_an_apple = true;
                score = score + 2;
                fruit = fruit + 1;
                cur_score = cur_score + 2;
                cur_fruit = cur_fruit + 1;
                Random rand1 = new Random();
                applex[i] = (rand1.nextFloat() * screen_width);
                appley[i] = (rand1.nextFloat() * screen_height);
                return i;
            }
        }
        return -1;
    }

    void addApples(Group root, int level){

        applex[0] = 400;    appley[0] = 200;
        applex[1] = 900;    appley[1] = 200;
        applex[2] = 600;    appley[2] = 400;
        applex[3] = 400;    appley[3] = 600;
        applex[4] = 900;    appley[4] = 600;

        applex[5] = 500;    appley[5] = 300;
        applex[6] = 750;    appley[6] = 300;
        applex[7] = 100;    appley[7] = 500;
        applex[8] = 500;    appley[8] = 700;
        applex[9] = 750;    appley[9] = 700;

        applex[10] = 100;    appley[10] = 300;
        applex[11] = 1000;    appley[11] = 100;
        applex[12] = 1100;    appley[12] = 400;
        applex[13] = 100;    appley[13] = 500;
        applex[14] = 1000;    appley[14] = 650;

        for(int i = 0; i < 15 ;i++){
            apples[i] = new Circle(10);
            //apples[i].setFill(Color.SALMON);
            apples[i].setCenterX(applex[i]);
            apples[i].setCenterY(appley[i]);
            if(i < 5){
                apples[i].setFill(Color.LIGHTSALMON);
            } else if(i < 10){
                apples[i].setFill(Color.SALMON);
            } else{
                apples[i].setFill(Color.DARKSALMON);
            }
        }


        for (int i = 0; i < 5; i++) {
            root.getChildren().add(apples[i]);
        }

        if (level >= 2){
            for(int i = 5; i < 10 ;i++){
                root.getChildren().add(apples[i]);
            }
        }
        if(level == 3){
            for(int i = 10; i < 15 ;i++){
                root.getChildren().add(apples[i]);
            }
        }
    }



    void countDown(Stage stage, SCENES scene, Group root){
        if(root != root3) {
            root.getChildren().add(timerLabel);
        }
        timeline1 = new Timeline();
        timeline1.setCycleCount(Timeline.INDEFINITE);
        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            // KeyFrame event handler
            public void handle(ActionEvent event) {
                if(root != root3) {
                    timeSeconds--;
                }
                // update timerLabel
                timerLabel.setText(
                        "Time Left: " + timeSeconds);
                if(isPaused){
                    timeline1.pause();
                } else{
                    timeline1.play();
                }
                if(returnToSplash){
                    timeline1.stop();
                }
                if (timeSeconds <= 0) {
                    timeline1.stop();
                    timeSeconds = 30;
                    if(cur_level == 1){
                        cur_level++;
                        cur_fruit = 0;
                        cur_score = 0;
                        dx = 2.0f;
                        dy = 2.0f;
                        root2.getChildren().add(timerLabel);
                        addApples(root2,2);
                        addSnake(root2,2);
                        root2.getChildren().add(scoreLable);
                        root2.getChildren().add(fruitLable);
                        root2.getChildren().add(cur_scoreLable);
                        root2.getChildren().add(cur_fruitLable);
                        setScene(stage, SCENES.LEVEL2);
                        timeline1.play();
                    } else if (cur_level == 2){
                        root3.getChildren().add(timerLabel);
                        dx = 4.0f;
                        dy = 4.0f;
                        cur_level++;
                        cur_fruit = 0;
                        cur_score = 0;
                        addApples(root3,3);
                        addSnake(root3,3);
                        root3.getChildren().add(scoreLable);
                        root3.getChildren().add(fruitLable);
                        root3.getChildren().add(cur_scoreLable);
                        root3.getChildren().add(cur_fruitLable);
                        setScene(stage, SCENES.LEVEL3);
                        timeline1.play();
                        timeSeconds = 1000;
                        root3.getChildren().remove(timerLabel);
                    } else if (cur_level == 3){
                        timeSeconds = 1000;
                    }
                }
            }
        };
        timeline1.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        handler));
        timeline1.playFromStart();

    }

    void addSnake(Group root, int level){
        if(level == 1){
            for(int i = 0; i < snake_len; i++){
                root1.getChildren().add(snake[i]);
            }
        } else if(level == 2){
            for(int i = 0; i < snake_len; i++){
                root2.getChildren().add(snake[i]);
            }
        }  else if(level == 3){
            for(int i = 0; i < snake_len; i++){
                root3.getChildren().add(snake[i]);
            }
        }
    }

    void setScene(Stage stage, SCENES scene) {
        switch(scene) {
            case SPLASH:
                stage.setTitle("splash");
                stage.setScene(splash);
                break;
            case LEVEL1:
                stage.setTitle("level 1");
                stage.setScene(level1);
                break;
            case LEVEL2:
                stage.setTitle("level 2");
                stage.setScene(level2);
                break;
            case LEVEL3:
                stage.setTitle("level 3");
                stage.setScene(level3);
                break;
            case ENDSCENE:
                stage.setTitle("Game Over");
                stage.setScene(endScene);
                break;
        }
    }

}

