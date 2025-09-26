# API Testing Scripts

This folder contains HTTP scripts for testing the Todo App API through the `/ask` endpoint. These scripts demonstrate how to perform CRUD operations on todos using natural language.

## Files Overview

### 1. `quick-start-examples.http`
**Best for beginners** - Contains 10 simple examples to get you started quickly:
- Basic todo creation
- Viewing todos
- Marking tasks as complete
- Deleting tasks

### 2. `todo-crud-operations.http`
**Comprehensive examples** - Covers all CRUD operations with various scenarios:
- Multiple ways to create todos (by category)
- Different ways to read/filter todos
- Update operations (mark complete, edit details)
- Delete operations (specific, bulk, by category)
- Complex operations and edge cases

### 3. `comprehensive-tool-testing.http`
**Technical testing** - Tests all underlying AI tools systematically:
- `fetchAllTodos` - Get all todos
- `fetchTodoById` - Get specific todo by ID
- `fetchTodosByCategory` - Filter by category (WORK, PERSONAL, SHOPPING, HEALTH, STUDY, HOME, OTHER)
- `makeTodo` - Create new todos
- `changeTodo` - Update existing todos
- `removeTodo` - Delete specific todos
- `removeAllTodo` - Delete all todos

## How to Use

### Prerequisites
1. Make sure the Todo App backend is running on `http://localhost:8080`
2. Use an HTTP client that supports `.http` files (VS Code with REST Client extension, IntelliJ IDEA, etc.)

### Using VS Code with REST Client Extension
1. Install the "REST Client" extension in VS Code
2. Open any `.http` file in this folder
3. Click "Send Request" above any HTTP request
4. View the response in a new tab

### Using IntelliJ IDEA
1. Open any `.http` file
2. Click the green arrow (▶️) next to any request
3. View the response in the HTTP Response tab

### Using curl (command line)
```bash
curl -X POST http://localhost:8080/api/v1/simple-chat/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Show me all my todos"}'
```

## API Endpoint

**Base URL**: `http://localhost:8080/api/v1/simple-chat`

**Endpoint**: `POST /ask`

**Request Format**:
```json
{
  "question": "Your natural language request here"
}
```

**Response Format**:
```json
{
  "question": "Your original question",
  "answer": "AI response with todo operations results"
}
```

## Natural Language Examples

### Creating Todos
- "I need to buy groceries for dinner"
- "Add finish quarterly report to my work tasks"
- "Schedule dentist appointment next week"
- "Remind me to call mom on Sunday"

### Viewing Todos
- "Show me all my todos"
- "What work tasks do I have?"
- "List my shopping items"
- "What do I need to complete today?"

### Updating Todos
- "Mark the grocery shopping as completed"
- "Update my dentist appointment to include the time: 2 PM"
- "I finished the quarterly report"

### Deleting Todos
- "Remove the completed shopping tasks"
- "Delete the task about calling mom"
- "Clear all my todos" (use with caution!)

## Tips for Testing

1. **Start with quick-start-examples.http** if you're new to the API
2. **Create some todos first** before testing read/update/delete operations
3. **Note the todo IDs** returned in responses for ID-specific operations
4. **Test error cases** to understand how the API handles invalid requests
5. **Try variations** of natural language to see how flexible the AI is

## Categories Available

The AI automatically categorizes todos into these categories:
- **WORK** - Professional tasks, meetings, reports
- **PERSONAL** - Family, friends, personal activities  
- **SHOPPING** - Groceries, purchases, errands
- **HEALTH** - Medical appointments, exercise, wellness
- **STUDY** - Learning, reading, courses, research
- **HOME** - Household maintenance, repairs, organization
- **OTHER** - Miscellaneous tasks that don't fit other categories

## Troubleshooting

### Common Issues:
1. **Connection refused**: Make sure the backend is running on port 8080
2. **500 Internal Server Error**: Check if the database is connected and AI service is configured
3. **Empty responses**: Verify your request format matches the examples
4. **AI not understanding**: Try rephrasing your natural language request

### Debug Steps:
1. Test with the simple "Hello" request first
2. Check the backend logs for any errors
3. Verify your JSON syntax is correct
4. Try simpler, more direct language in your requests

## Example Session

Here's a typical testing session flow:

1. **Test Connection**:
   ```json
   {"question": "Hello, how can you help me?"}
   ```

2. **Create Some Todos**:
   ```json
   {"question": "I need to buy milk, schedule a meeting with John, and finish my homework"}
   ```

3. **View Created Todos**:
   ```json
   {"question": "Show me all my todos"}
   ```

4. **Update a Todo**:
   ```json
   {"question": "Mark the milk purchase as completed"}
   ```

5. **Delete a Todo**:
   ```json
   {"question": "Remove the homework task"}
   ```

This systematic approach helps verify that all CRUD operations work correctly through the natural language interface.
