package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dragon.GameManager;
import dragon.Pet;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {

  @FXML
  private Pane gamePane;
  @FXML
  private ImageView player;
  @FXML
  private Label scoreLabel;
  @FXML
  private Label roundLabel;

  String petTypeString = "";
  private Image characterFire;
  private Image characterStandBy;
  private String petID;

  private final List<ImageView> enemies = new ArrayList<>();
  private ImageView bullet;
  private int score = 0;
  private int round = 0; // 当前回合
  private final int baseEnemyCount = 3; // 每回合的基础敌机数量
  private Timeline bulletTimeline;
  private ScheduledExecutorService enemyGeneratorThread; // 定时任务线程
  // private Pet p = new Pet("test");

  @FXML
  public void initialize() {
    // 初始化回合和分数
    scoreLabel.setText("Score: " + score);
    roundLabel.setText("Round: " + round);

    GameManager.getSoundManager().playSong("battle_0");
    Pet p = GameManager.getInstance().getPlayer().pets.get(GameManager.selected);

    int petType = p.getType();
    petID = p.getID();

    String petTypeString = "";

    if (petType == 0) {
      petTypeString = "dragon";
    } else if (petType == 1) {
      petTypeString = "dogs";
    } else if (petType == 2) {
      petTypeString = "cats";
    }

    URL urlStandBy = ActiveAnimation.class
        .getResource("/pets/" + petTypeString + "/" + petID + "/" + petID + "__" + "standby" + "_1" + ".gif");
    characterStandBy = new Image(urlStandBy.toExternalForm());
    player.setImage(characterStandBy);
    if (petID != "Balerion") {
      player.setScaleX(-1);
    }

    URL urlFire = ActiveAnimation.class
        .getResource("/pets/" + petTypeString + "/" + petID + "/" + petID + "__" + "angry" + "_1" + ".gif");
    characterFire = new Image(urlFire.toExternalForm());

    startEnemyGeneration();

    // 监听键盘事件
    gamePane.setOnKeyPressed(event -> {
      double gamePaneWidth = gamePane.getPrefWidth();
      double gamePaneHeight = gamePane.getPrefHeight();
      switch (event.getCode()) {
        case LEFT:
          if (player.getLayoutX() > 0)
            player.setLayoutX(player.getLayoutX() - 10);
          break;
        case RIGHT:
          if (player.getLayoutX() + 50 < gamePaneWidth)
            player.setLayoutX(player.getLayoutX() + 10);
          break;
        case UP:
          if (player.getLayoutY() > 0)
            player.setLayoutY(player.getLayoutY() - 10); // 向上移动
          break;
        case DOWN:
          if (player.getLayoutY() + 50 < gamePaneHeight)
            player.setLayoutY(player.getLayoutY() + 10); // 向下移动
          break;
        case SPACE:
          shoot();
          break;
        default:
          // 如果有其他按键可以在这里处理
          break;
      }
    });

    gamePane.setFocusTraversable(true);

    // 敌机随机移动
    startEnemyMovement();
  }

  private void shoot() {
    if (bullet != null)
      return; // 只允许一颗子弹存在

    player.setImage(characterFire);
    if (petID != "Balerion") {
      player.setScaleX(-1);
    }

    bullet = new ImageView("fire.png");

    bullet.setFitWidth(22);
    bullet.setFitHeight(50);
    bullet.setRotate(270);

    bullet.setLayoutX(player.getLayoutX() + 17.5); // 子弹居中
    bullet.setLayoutY(player.getLayoutY() - 15);
    gamePane.getChildren().add(bullet);

    // 清理旧的 Timeline
    if (bulletTimeline != null) {
      bulletTimeline.stop();
    }
    // 子弹向上移动
    bulletTimeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
      if (bullet != null) {
        // bullet.setLayoutY(bullet.getLayoutY() - 5);
        bullet.setLayoutX(bullet.getLayoutX() + 10);
        if (bullet.getLayoutX() > gamePane.getWidth()) {
          ;
          gamePane.getChildren().remove(bullet);
          bullet = null;
          bulletTimeline.stop(); // 停止 Timeline
        }
        checkCollision();
      }
    }));
    bulletTimeline.setCycleCount(Timeline.INDEFINITE);
    bulletTimeline.play();

    PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
    pause.setOnFinished(event -> player.setImage(characterStandBy));
    pause.play();
  }

  private void checkCollision() {
    if (bullet == null)
      return;

    for (ImageView enemy : enemies) {
      if (bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
        gamePane.getChildren().removeAll(bullet, enemy);
        enemies.remove(enemy);
        bullet = null; // 在移除子弹后设置为 null
        score += 10;
        scoreLabel.setText("Score: " + score);
        return; // 碰撞后立即退出，避免继续检查
      }
    }
  }

  // private void startEnemyMovement() {
  // Random random = new Random();
  // AnimationTimer timer = new AnimationTimer() {
  // @Override
  // public void handle(long now) {
  // for (ImageView enemy : enemies) {
  // enemy.setLayoutY(enemy.getLayoutY() + (random.nextBoolean() ? 1 : -1) *
  // random.nextInt(30));
  // }
  // }
  // };
  // timer.start();
  // }
  private void startEnemyMovement() {
    Random random = new Random();
    Timeline movementTimeline = new Timeline(new KeyFrame(Duration.millis(70), event -> {
      for (ImageView enemy : enemies) {
        double currentY = enemy.getLayoutY();
        double jumpOffset = (random.nextBoolean() ? 1 : -1) * random.nextInt(100); // 随机跳跃距离
        double newY = currentY + jumpOffset;

        // 确保敌人不会超出窗口范围
        if (newY < 0) {
          newY = 0; // 上边界
        } else if (newY + enemy.getFitHeight() > gamePane.getHeight()) {
          newY = gamePane.getHeight() - enemy.getFitHeight(); // 下边界
        }

        enemy.setLayoutY(newY);
      }
    }));

    movementTimeline.setCycleCount(Timeline.INDEFINITE); // 无限循环
    movementTimeline.play();
  }

  private void generateEnemies() {
    // 计算敌机数量
    int enemyCount = baseEnemyCount + round * 2;

    // 生成敌机
    for (int i = 0; i < enemyCount; i++) {
      // Rectangle enemy = new Rectangle(50, 50);
      // enemy.setFill(javafx.scene.paint.Color.RED);

      Image enemyImage = new Image(
          Objects.requireNonNull(getClass().getResource("/minigame_knight.png")).toExternalForm());
      ImageView enemy = new ImageView(enemyImage);
      double width = 80.6;
      double height = 110.3;
      enemy.setScaleX(-1);
      // enemy.setFitWidth(width);
      // enemy.setFitHeight(height);

      // 随机生成位置（避免重叠）
      double x = 500 + (i % 5) * 75; // 横向间隔
      double y = 100 + (i / 5) * 130; // 纵向间隔
      enemy.setLayoutX(x);
      enemy.setLayoutY(y);

      enemies.add(enemy);
      gamePane.getChildren().add(enemy);
    }
    round += 1;
    roundLabel.setText("Round: " + round);
  }

  private void startEnemyGeneration() {
    enemyGeneratorThread = Executors.newSingleThreadScheduledExecutor();

    enemyGeneratorThread.scheduleAtFixedRate(() -> {
      // 如果敌机列表为空，并且回合数达到指定值，则结束游戏
      if (enemies.isEmpty() && round >= 1) {
        enemyGeneratorThread.shutdown(); // 停止线程
        // 调用 gameFinish 方法
        Platform.runLater(() -> {
          try {
            gameFinish("Game Completed", "return to dashboard");
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
        return;
      }

      // 如果敌机列表为空，但回合数未达到指定值，则生成新的敌机
      if (enemies.isEmpty()) {
        Platform.runLater(this::generateEnemies);
      }

    }, 0, 100, TimeUnit.MILLISECONDS); // 每0.1秒执行一次
  }

  private void gameFinish(String title, String message) throws IOException {
    // 创建一个新窗口（Stage）
    Stage dialogStage = new Stage();
    dialogStage.setTitle(title);
    dialogStage.initModality(Modality.WINDOW_MODAL); // 模态窗口

    // 布局
    VBox vbox = new VBox(10); // 外部布局
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    // 添加标题标签
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

    // 添加内容标签
    Label messageLabel = new Label(message);
    messageLabel.setWrapText(true);
    messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
    messageLabel.setMaxWidth(350); // 限制内容宽度，避免过宽
    messageLabel.setAlignment(Pos.CENTER);

    // 添加关闭按钮
    Button closeButton = new Button("Close");
    closeButton.setOnAction(event -> {
      dialogStage.close();
      try {
        App.loadDashboard();
      } catch (Exception e) {
      }
    });
    closeButton.setPrefWidth(100); // 设置按钮宽度
    VBox.setMargin(closeButton, new Insets(50, 0, 0, 0)); // 将按钮向下移动

    // 布局添加
    vbox.getChildren().addAll(titleLabel, messageLabel, closeButton);

    // 设置场景
    Scene scene = new Scene(vbox, 400, 250);
    dialogStage.setScene(scene);

    // 显示窗口
    dialogStage.showAndWait();
    GameManager.getSoundManager().stopSong();
    GameManager.getSoundManager().playRandomSong();
  }

  @FXML
  private void back() throws IOException {
    // triger backend method
    GameManager m = GameManager.getInstance();
    m.getPlayer().pets.get(GameManager.selected).hasGame = true;
    m.getPlayer().pets.get(GameManager.selected).combatFeedback(score);
    GameManager.getSoundManager().stopSong();
    GameManager.getSoundManager().playRandomSong();
    GameManager.getInstance().getSoundManager().playSound("menu_back");

    App.loadDashboard();

  }
}
