export const mockUser = {
  email: "thomas@example.com",
  password: "password123",
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
  themes: [
    { name: "dark", price: 1000, image: "theme-dark.jpg" },
    { name: "ocean", price: 500, image: "theme-ocean.jpg" },
    { name: "fire", price: 700, image: "theme-fire.jpg" },
    { name: "sky", price: 1000, image: "theme-sky.jpg" },
  ],
};
