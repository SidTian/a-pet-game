package dragon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class GameManager implements Serializable {
  private static final long serialVersionUID = 1L; // 推荐：定义一个序列化版本号
  private static final String SAVE_FILE = "src/main/resources/saves/players.dat";
  public static int selected = -1;
  private static Player player;
  public static SoundManager soundManager = new SoundManager();
  // 单例实例
  private static volatile GameManager instance;

  // 私有构造函数
  private GameManager() {
    // 第一次创建时尝试从文件加载 players
    if (player == null) {
      // initGameSetting();
      loadGame();

      // ParentControl parentControl = new ParentControl();
      if (player == null)
        player = new Player();
      // System.out.println("创建新的玩家列表");
      // } else {
      // System.out.println("从文件加载玩家列表成功");
      // }
    }
  }

  // 提供一个获取唯一实例的静态方法
  public static GameManager getInstance() {
    if (instance == null) {
      synchronized (GameManager.class) {
        if (instance == null) {
          instance = new GameManager();
        }
      }
    }
    return instance;
  }

  public Player getPlayer() {
    return player;
  }

  public void saveGame() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
      oos.writeObject(player);
      System.out.println("对象已序列化并存储到 person.dat 文件中");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  public void loadGame() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
      player = (Player) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void initGameSetting() {
    Player p = new Player();
    ArrayList<Pet> pets = new ArrayList<>();
    for (int i = 0; i < 5; i++)
      pets.add(new Pet("test"));
    p.pets = pets;
    player = p;
    saveGame();
    loadGame();
  }

  // 防止反序列化破坏单例
  protected Object readResolve() {
    return getInstance();
  }

  public static SoundManager getSoundManager() {
    return soundManager;
  }

  public static void setSelected(int selected) {
    GameManager.selected = selected;
  }
}
