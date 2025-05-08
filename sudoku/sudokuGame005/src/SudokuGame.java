import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/*
 * 项目名称：数独 - Java Swing 版本
 * 文件说明：本文件为数独游戏的 Java Swing 图形界面实现。
 * 作者：Zdw1922994639
 * 创建时间：2025年
 *
 * 版权所有 (c) 2025 Zdw1922994639
 *
 * 本项目基于 Java 语言开发，使用 Java Swing 进行图形用户界面设计。
 *
 * 如有建议或问题，请联系作者。
 */

// 新增的 LoginRegisterFrame 类
class LoginRegisterFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, String> userDatabase; // 用户名和密码存储
    private final String USER_DATA_FILE = "users.txt"; // 用户数据文件

    public LoginRegisterFrame() {
        userDatabase = new HashMap<>();
        loadUserDatabase(); // 加载用户数据
        initializeUI();
    }

    // 加载用户数据库从文件
    private void loadUserDatabase() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile(); // 创建文件如果不存在
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "无法创建用户数据文件。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // 跳过空行
                String[] parts = line.split(":", 2);
                if (parts.length >= 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    userDatabase.put(username, password);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "读取用户数据时出现错误。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 将新用户写入文件
    private void saveUserToFile(String username, String password) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_FILE, true))) {
            bw.write(username + ":" + password);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "保存用户数据时出现错误。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setTitle("登录与注册");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(245, 245, 245));

        // 登录面板
        JPanel loginPanel = createLoginPanel();

        // 注册面板
        JPanel registerPanel = createRegisterPanel();

        // 添加面板到主面板
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 增加组件间距
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(userLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        JTextField loginUsernameField = new JTextField(15);
        panel.add(loginUsernameField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("密码:");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(passLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        JPasswordField loginPasswordField = new JPasswordField(15);
        panel.add(loginPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton loginButton = new JButton("登录");
        styleButton(loginButton);
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        JButton toRegisterButton = new JButton("去注册");
        styleButton(toRegisterButton);
        panel.add(toRegisterButton, gbc);

        // 切换到注册面板
        toRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        // 登录按钮动作
        loginButton.addActionListener(e -> {
            String username = loginUsernameField.getText().trim();
            String password = new String(loginPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空。", "登录失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userDatabase.containsKey(username) || !userDatabase.get(username).equals(password)) {
                JOptionPane.showMessageDialog(this, "用户名或密码错误。", "登录失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "登录成功！欢迎进入游戏。", "登录成功", JOptionPane.INFORMATION_MESSAGE);
            // 打开游戏窗口
            new SudokuGame();
            dispose(); // 关闭登录窗口
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 增加组件间距
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(userLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        JTextField registerUsernameField = new JTextField(15);
        panel.add(registerUsernameField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("密码:");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(passLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        JPasswordField registerPasswordField = new JPasswordField(15);
        panel.add(registerPasswordField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel confirmPassLabel = new JLabel("确认密码:");
        confirmPassLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        panel.add(confirmPassLabel, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        JPasswordField registerConfirmPasswordField = new JPasswordField(15);
        panel.add(registerConfirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton registerButton = new JButton("注册");
        styleButton(registerButton);
        panel.add(registerButton, gbc);

        gbc.gridy = 5;
        JButton toLoginButton = new JButton("去登录");
        styleButton(toLoginButton);
        panel.add(toLoginButton, gbc);

        // 切换到登录面板
        toLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // 注册按钮动作
        registerButton.addActionListener(e -> {
            String username = registerUsernameField.getText().trim();
            String password = new String(registerPasswordField.getPassword()).trim();
            String confirmPassword = new String(registerConfirmPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空。", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "密码不匹配。", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userDatabase.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "用户名已存在。", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userDatabase.put(username, password);
            saveUserToFile(username, password); // 保存新用户到文件
            JOptionPane.showMessageDialog(this, "注册成功！请登录。", "注册成功", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "login");
        });

        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }
}

// 新增的 HelpDialog 类
class HelpDialog extends JDialog {
    public HelpDialog(JFrame parent) {
        super(parent, "帮助", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        JTextArea helpTextArea = new JTextArea();
        helpTextArea.setEditable(false);
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        helpTextArea.setBackground(new Color(245, 245, 245));
        helpTextArea.setText("欢迎来到数独游戏！\n\n"
                + "游戏规则：\n"
                + "- 在每个单元格中填入数字，使每行、每列和每个宫格内的数字都不重复。\n\n"
                + "操作说明：\n"
                + "- 选择游戏模式和难度，填写答案，提交检查或保存/加载游戏。\n"
                + "- 提示功能可以帮助你解决难题。\n"
                + "- 重新开始游戏可以重新生成新的数独谜题。\n"
                + "- 计时器会记录你完成谜题所用的时间。\n\n"
                + "祝你游戏愉快！");

        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("关闭");
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }
}

public class SudokuGame extends JFrame {
    private int size;
    private int[][] puzzle;
    private int[][] solution;
    private JTextField[][] cells;
    private boolean isEasyMode;
    private JMenuBar menuBar;
    private JMenu gameControlMenu, gameSettleMenu, modeMenu, settingsMenu, helpMenu;
    private JMenuItem easyMenuItem, mediumMenuItem, hardMenuItem, restartMenuItem, exitMenuItem, solveMenuItem, saveMenuItem, loadMenuItem, hintMenuItem, helpMenuItem;
    private JLabel statusLabel;
    private Timer timer;
    private int elapsedTime;

    public SudokuGame() {
        isEasyMode = true; // 默认简单模式
        initializeMenu();
        initializeStatusLabel();
        setSize(600, 650);
        setTitle("数独游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中
        setVisible(true);
        initializeGame(4); // 默认初始化为简单模式
    }

    private void initializeMenu() {
        menuBar = new JMenuBar();

        gameControlMenu = new JMenu("游戏控制");
        gameSettleMenu = new JMenu("游戏结算");
        modeMenu = new JMenu("选择模式");
        settingsMenu = new JMenu("设置");
        helpMenu = new JMenu("帮助"); // 添加帮助菜单

        easyMenuItem = new JMenuItem("简单模式 (4x4)");
        mediumMenuItem = new JMenuItem("中等模式 (6x6)");
        hardMenuItem = new JMenuItem("困难模式 (9x9)");
        restartMenuItem = new JMenuItem("重新开始");
        exitMenuItem = new JMenuItem("退出游戏");
        solveMenuItem = new JMenuItem("提交答案");
        saveMenuItem = new JMenuItem("保存游戏");
        loadMenuItem = new JMenuItem("加载游戏");
        hintMenuItem = new JMenuItem("提示");
        helpMenuItem = new JMenuItem("帮助"); // 添加帮助菜单项

        easyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEasyMode = true;
                initializeGame(4);
            }
        });

        mediumMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEasyMode = false;
                initializeGame(6);
            }
        });

        hardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEasyMode = false;
                initializeGame(9);
            }
        });

        restartMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeGame(size);
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        solveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solvePuzzle();
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        hintMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                provideHint();
            }
        });

        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        });

        modeMenu.add(easyMenuItem);
        modeMenu.add(mediumMenuItem);
        modeMenu.add(hardMenuItem);

        gameControlMenu.add(restartMenuItem);
        gameControlMenu.add(saveMenuItem);
        gameControlMenu.add(loadMenuItem);
        gameControlMenu.add(hintMenuItem);

        gameSettleMenu.add(solveMenuItem);

        settingsMenu.add(exitMenuItem);

        helpMenu.add(helpMenuItem);

        menuBar.add(gameControlMenu);
        menuBar.add(gameSettleMenu);
        menuBar.add(modeMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void initializeStatusLabel() {
        statusLabel = new JLabel("时间: 0 秒");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // 添加边距
        menuBar.add(Box.createHorizontalGlue()); // 将时间标签推到右侧
        menuBar.add(statusLabel);
    }

    private void initializeGame(int size) {
        this.size = size;
        this.puzzle = new int[size][size];
        this.solution = new int[size][size];
        this.cells = new JTextField[size][size];
        getContentPane().removeAll();
        setLayout(new GridLayout(size, size));
        generatePuzzle();
        initializeBoard();
        revalidate();
        repaint();
        startTimer();
    }

    private void generatePuzzle() {
        int[][] board = new int[size][size];
        fillBoard(board);
        puzzle = board;
        copyBoard(board, solution);
        removeNumbers();
    }

    private boolean fillBoard(int[][] board) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= size; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < size; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }
        int sqrt = (int) Math.sqrt(size);
        int boxRowStart = row - row % sqrt;
        int boxColStart = col - col % sqrt;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                if (board[r][d] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers() {
        Random random = new Random();
        int count;
        if (size == 4) {
            count = 8;
        } else if (size == 6) {
            count = 18;
        } else {
            count = 40;
        }

        while (count != 0) {
            int cellId = random.nextInt(size * size);
            int row = cellId / size;
            int col = cellId % size;
            if (puzzle[row][col] != 0) {
                int temp = puzzle[row][col];
                puzzle[row][col] = 0;
                if (hasUniqueSolution(puzzle)) {
                    count--;
                } else {
                    puzzle[row][col] = temp;
                }
            }
        }
    }

    private boolean hasUniqueSolution(int[][] board) {
        int[][] tempBoard = new int[size][size];
        copyBoard(board, tempBoard);
        return solveBoard(tempBoard) == 1;
    }

    private int solveBoard(int[][] board) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    int count = 0;
                    for (int num = 1; num <= size; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            count += solveBoard(board);
                            if (count > 1) return count;
                            board[row][col] = 0;
                        }
                    }
                    return count;
                }
            }
        }
        return 1;
    }

    private void initializeBoard() {
        getContentPane().removeAll();
        setLayout(new GridLayout(size, size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("微软雅黑", Font.BOLD, 20));
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                if (puzzle[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(puzzle[i][j]));
                    cells[i][j].setEditable(false);
                    cells[i][j].setBackground(new Color(220, 220, 220));
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setBackground(Color.WHITE);
                }
                add(cells[i][j]);
            }
        }
        revalidate();
        repaint();
    }

    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, size);
        }
    }

    //提交答案
    private void solvePuzzle() {
        boolean hasErrors = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String currentText = cells[i][j].getText().trim();
                String solutionText = String.valueOf(solution[i][j]);
                if (!currentText.equals(solutionText)) {
                    cells[i][j].setBackground(new Color(255, 102, 102)); // 红色表示错误
                    hasErrors = true;
                } else {
                    if (puzzle[i][j] == 0) {
                        cells[i][j].setBackground(Color.WHITE);
                    } else {
                        cells[i][j].setBackground(new Color(220, 220, 220));
                    }
                }
            }
        }
        if (hasErrors) {
            JOptionPane.showMessageDialog(this, "部分答案不正确，请检查标记的单元格。", "提交", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "所有答案均正确！用时：" + elapsedTime + "秒", "提交", JOptionPane.INFORMATION_MESSAGE);
            elapsedTime = 0;
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        elapsedTime = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                statusLabel.setText("时间: " + elapsedTime + " 秒");
            }
        });
        timer.start();
    }

    //保存游戏
    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("sudoku_save.dat"))) {
            out.writeObject(puzzle);
            out.writeObject(solution);
            out.writeInt(elapsedTime);
            out.writeInt(size);  // 保存大小

            // 保存用户填写的答案
            int[][] currentAnswers = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (!cells[i][j].getText().isEmpty()) {
                        currentAnswers[i][j] = Integer.parseInt(cells[i][j].getText());
                    } else {
                        currentAnswers[i][j] = 0;
                    }
                }
            }
            out.writeObject(currentAnswers);

            JOptionPane.showMessageDialog(this, "游戏保存成功。", "保存游戏", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "保存游戏时出现错误。", "保存游戏", JOptionPane.ERROR_MESSAGE);
        }
    }

    //加载游戏
    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("sudoku_save.dat"))) {
            puzzle = (int[][]) in.readObject();
            solution = (int[][]) in.readObject();
            elapsedTime = in.readInt();
            size = in.readInt();  // 读取大小
            int[][] currentAnswers = (int[][]) in.readObject(); // 读取用户填写的答案
            cells = new JTextField[size][size];  // 重新初始化文本字段数组
            initializeBoard();  // 重新初始化板

            // 恢复用户填写的答案
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (currentAnswers[i][j] != 0) {
                        cells[i][j].setText(String.valueOf(currentAnswers[i][j]));
                    }
                }
            }

            startLoadedTimer();  // 重新启动计时器
            JOptionPane.showMessageDialog(this, "游戏加载成功。", "加载游戏", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "加载游戏时出现错误。", "加载游戏", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startLoadedTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                statusLabel.setText("时间: " + elapsedTime + " 秒");
            }
        });
        timer.start();
    }

    private void provideHint() {
        Random random = new Random();
        int row = random.nextInt(size);
        int col = random.nextInt(size);
        if (puzzle[row][col] == 0) {
            cells[row][col].setText(String.valueOf(solution[row][col]));
            cells[row][col].setBackground(new Color(144, 238, 144)); // 绿色背景表示提示
        } else {
            provideHint();
        }
    }


    private void showHelp() {
        HelpDialog helpDialog = new HelpDialog(this);
        helpDialog.setVisible(true);
    }

    public static void main(String[] args) {
        // 启动登录注册界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginRegisterFrame();
            }
        });
    }
}
