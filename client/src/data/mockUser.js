let mockUser = {
  id: 1,
  email: "thomas@example.com",
  password: "password123",
  name: "Thomas",
  firstname: "Thomas",
  lastname: "Muller",
  nickname: "Thomas13",
  points: 1500,
  purchasedThemes: ["default", "dark"],
  progress: {
    difficulty: "EASY",
    topic: "SELECT",
    tasksSolved: 12,
    totalTasks: 37,
  },
  tasks: [
    { type: "EASY", theme: "SELECT", completed: 3, total: 5 },
    { type: "EASY", theme: "SELECT", completed: 4, total: 5 },
  ],
  completedTasks: {
    easy: [],
    medium: [],
    hard: [],
  },
  completedLevels: {
    easy: false,
    medium: false,
    hard: false,
  },
};

// Список доступных тем для покупки
export const availableThemes = [
  { id: "default", name: "Default", price: 0, image: "default-bg.jpg" },
  { id: "dark", name: "Dark", price: 0, image: "dark-bg.jpg" },
  { id: "ocean", name: "Ocean", price: 500, image: "ocean-bg.jpg" },
  { id: "forest", name: "Forest", price: 1000, image: "forest-bg.jpg" },
];

// Функция для получения пользователя (симуляция запроса к БД)
export const getUser = () => {
  return mockUser;
};

// Функция для обновления пользователя (симуляция сохранения в БД)
export const updateUser = (updatedUser) => {
  mockUser = { ...mockUser, ...updatedUser };
};

// Функция для сброса данных пользователя при выходе
export const resetUser = () => {
  mockUser = {
    id: 1,
    email: "thomas@example.com",
    password: "password123",
    name: "Thomas",
    firstname: "Thomas",
    lastname: "Muller",
    nickname: "Thomas13",
    points: 1500,
    purchasedThemes: ["default", "dark"],
    progress: {
      difficulty: "EASY",
      topic: "SELECT",
      tasksSolved: 12,
      totalTasks: 37,
    },
    tasks: [
      { type: "EASY", theme: "SELECT", completed: 3, total: 5 },
      { type: "EASY", theme: "SELECT", completed: 4, total: 5 },
    ],
    completedTasks: {
      easy: [],
      medium: [],
      hard: [],
    },
    completedLevels: {
      easy: false,
      medium: false,
      hard: false,
    },
  };
};