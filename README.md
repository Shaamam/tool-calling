# Todo App with Spring AI Tool Calling

A sophisticated Todo management application that demonstrates the power of Spring AI with Tool Calling capabilities. This application allows users to manage their todos using natural language through an AI-powered chat interface.

## Features

- **Natural Language Todo Management**: Interact with your todos using conversational AI
- **Intelligent Task Categorization**: Automatically categorizes tasks into WORK, PERSONAL, SHOPPING, HEALTH, STUDY, HOME, or OTHER
- **Full CRUD Operations**: Create, read, update, and delete todos through natural language commands
- **Modern React Frontend**: Beautiful and responsive user interface
- **RESTful API**: Backend powered by Spring Boot with Spring AI integration
- **Tool Calling**: AI model can directly invoke backend functions to manage todos

## Technology Stack

- **Backend**: Spring Boot 3.5.5, Spring AI, Java 21
- **Frontend**: React, TypeScript, Tailwind CSS
- **Database**: PostgreSQL (via JPA/Hibernate)
- **AI Integration**: Google Vertex AI Gemini
- **Build Tool**: Gradle

## API Endpoints

### Chat Endpoint
- `POST /api/v1/simple-chat/ask` - Main endpoint for natural language todo management

### Available Todo Operations via Chat
- **Create Todo**: "I need to buy groceries for dinner"
- **List Todos**: "Show me all my todos" or "What tasks do I have?"
- **Filter by Category**: "Show me my work tasks"
- **Update Todo**: "Mark 'buy groceries' as completed"
- **Delete Todo**: "Remove the grocery shopping task"
- **Delete All**: "Clear all my todos"

## Getting Started

### Prerequisites
- Java 21
- Node.js and npm
- PostgreSQL database
- Google Cloud credentials for Vertex AI

### Backend Setup
1. Clone the repository
2. Configure your database connection in `application.properties`
3. Set up Google Cloud credentials for Vertex AI
4. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```

### Using Make Commands
The project includes a Makefile for common tasks:
- `make build` - Build the entire application
- `make run` - Run the backend application
- `make frontend` - Start the frontend development server

## Usage Examples

### Natural Language Commands
- **Creating Todos**:
  - "I need to schedule a dentist appointment next week"
  - "Add finish quarterly report to my work tasks"
  - "Remind me to buy milk and bread"

- **Viewing Todos**:
  - "What are my pending tasks?"
  - "Show me my health-related todos"
  - "List all completed tasks"

- **Updating Todos**:
  - "Mark the dentist appointment as done"
  - "Update my grocery list to include eggs"

- **Deleting Todos**:
  - "Remove the completed shopping tasks"
  - "Delete all my todos"

## Project Structure

```
├── src/main/java/io/shaama/todoapp/
│   ├── TodoappApplication.java          # Main Spring Boot application
│   ├── chat/
│   │   ├── ChatBotController.java       # REST controller for chat endpoint
│   │   ├── ChatService.java             # AI chat service with tool calling
│   │   ├── ChatBotRequest.java          # Request DTO
│   │   └── ChatBotResponse.java         # Response DTO
│   └── todo/
│       ├── Todo.java                    # Todo entity
│       ├── TodoRepository.java          # Data repository
│       ├── TodoService.java             # Business logic
│       └── TodoTools.java               # AI tools for todo operations
├── frontend/
│   ├── src/
│   │   ├── components/chat/
│   │   │   └── ChatWindow.tsx           # Main chat interface
│   │   ├── axiosClient.ts               # API client configuration
│   │   └── App.tsx                      # Main React component
│   └── package.json
├── requests/                            # HTTP scripts for API testing
└── README.md
```
