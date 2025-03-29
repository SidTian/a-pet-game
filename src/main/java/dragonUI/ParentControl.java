package dragonUI;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import dragon.GameManager;
import dragon.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ParentControl implements Initializable {

  @FXML
  private CheckBox guardianCheckBox; // 绑定 FXML 中的 CheckBox

  @FXML
  private Label statusLabel; // 绑定 FXML 中的 Label

  @FXML
  private ComboBox<Integer> startHourPicker;

  @FXML
  private ComboBox<Integer> startMinutePicker;

  @FXML
  private ComboBox<Integer> endHourPicker;

  @FXML
  private ComboBox<Integer> endMinutePicker;

  @FXML
  private ComboBox<String> petRevivePicker;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    guardianCheckBox.setSelected(GameManager.getInstance().getPlayer().isGuardian);
    // comboBox.setValue("Option 1");
    initTimePicker();
    initPetRevivePicker();
  }

  public void initTimePicker() {
    for (int i = 0; i < 24; i++) {
      startHourPicker.getItems().add(i);
      endHourPicker.getItems().add(i);
    }

    for (int i = 0; i < 60; i++) {
      startMinutePicker.getItems().add(i);
      endMinutePicker.getItems().add(i);
    }

    GameManager m = GameManager.getInstance();
    Player p = m.getPlayer();
    LocalTime st = p.ValidStartTime;
    LocalTime et = p.ValidEndTime;
    startHourPicker.setValue(st.getHour()); // 设置开始时间的小时
    startMinutePicker.setValue(st.getMinute()); // 设置开始时间的分钟
    endHourPicker.setValue(et.getHour()); // 设置结束时间的小时
    endMinutePicker.setValue(et.getMinute()); // 设置结束时间的分钟
  }

  public void initPetRevivePicker() {
    Player p = GameManager.getInstance().getPlayer();
    for (int i = 0; i < p.pets.size(); i++) {
      if (p.pets.get(i).getHealth().getValue() <= 0) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pet id: " + i);
        sb.append("|Pet name: " + p.pets.get(i).getName());
        sb.append("|Pet type: " + p.pets.get(i).getType());
        petRevivePicker.getItems().add(sb.toString());
      }
    }
    if (petRevivePicker.getItems().isEmpty()) {
      petRevivePicker.getItems().add("empty");
    }
  }

  @FXML
  private void switchToMain() throws IOException {
    App.loadMain();
  }

  @FXML
  private void guardianAction() {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    GameManager m = GameManager.getInstance();
    if (guardianCheckBox.isSelected()) {
      m.getPlayer().isGuardian = true;
      statusLabel.setText("guardian status: true"); // 更新状态
    } else {
      m.getPlayer().isGuardian = false;
      statusLabel.setText("guardian status: false"); // 更新状态
    }
  }

  @FXML
  private void setTime() {
    GameManager.getInstance().getSoundManager().playSound("menu_select");
    Integer startHour = startHourPicker.getValue();
    Integer startMinute = startMinutePicker.getValue();
    Integer endHour = endHourPicker.getValue();
    Integer endMinute = endMinutePicker.getValue();
    // 检查输入值是否有效
    if (startHour == null || startMinute == null || endHour == null || endMinute == null) {
      System.err.println("Invalid input: All time fields must be selected.");
      App.showDialog("warning", "You must sett all field for time");
      return;
    }

    try {
      // 创建 LocalTime 对象
      LocalTime startTime = LocalTime.of(startHour, startMinute);
      LocalTime endTime = LocalTime.of(endHour, endMinute);

      // 获取玩家对象并设置有效时间段
      Player p = GameManager.getInstance().getPlayer();
      p.setValidTimePeriod(startTime, endTime);

      System.out.println("Valid time period set: " + startTime + " to " + endTime);
    } catch (Exception e) {
      System.err.println("Error while setting valid time period: " + e.getMessage());
    }
  }

  @FXML
  private void petRevive() {
    GameManager.getInstance().getSoundManager().playSound("pet_revive");

    if (petRevivePicker.getValue() == null) {
      App.showDialog("warning", "please select a pet");
      return;
    }
    if (!petRevivePicker.getValue().equals("empty")) {
      String value = petRevivePicker.getValue(); // 获取字符串
      char c = value.charAt(8); // 获取索引为 8 的字符
      int selectedPet = Character.getNumericValue(c); // 将字符转为对应的数字
      GameManager.getInstance().getPlayer().pets.get(selectedPet).petRevive();
      App.showDialog("Success", "The pet has been revived successfully");
      petRevivePicker.getItems().clear();
      initPetRevivePicker();
    } else
      App.showDialog("warning", "you don't have any pet to revive");
  }
}