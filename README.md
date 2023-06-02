# **User Service**
The User Service provides RESTful endpoints to perform CRUD operations on user entities.

## **Installation**
Clone the repository: **git clone https://github.com/Numaira-Nawaz/User-Service.git**

Navigate to the project directory: cd User-Service

Build the project: mvn clean install

## **Usage**

Start the application: mvn spring-boot:run

Access the API endpoints through http://localhost:8080/v1/user

## **Endpoints.**

### **POST /v1/user**
Creates a new user.

#### Request Body:
firstName (required): The first name of the user.

lastName (required): The last name of the user.

#### Response:

**201 Created**: User created successfully. Response body contains the created user details.
**409 Conflict**: If the username is already taken.

### **GET /v1/user/{id}**
Returns the user with the specified ID.

#### Path Parameters:
id (required): The ID of the user to retrieve.
#### Response:

**200 OK**: User found. Response body contains the user details.
**404 Not Found**: User not found.

### **GET /v1/user/allUser**
Returns a list of all users.

#### Response:

**200 OK**: List of users returned successfully.

### **PUT /v1/user/{id}**
Updates the user with the specified ID.

#### Path Parameters:

id (required): The ID of the user to update.
Request Body:

firstName (required): The updated first name of the user.

lastName (required): The updated last name of the user.

#### Response:

**200 OK**: User updated successfully. Response body contains the updated user details.
**404 Not Found**: User not found.
**409 Conflict**: If the username is already taken.

### **DELETE /v1/user/{id}**
Deletes the user with the specified ID.

#### Path Parameters:

id (required): The ID of the user to delete.
#### Response:

**200 OK**: User deleted successfully.
**202 Accepted**: User not found.

**Note:** In the case where a user is not found with the given ID, the response will be 202 Accepted instead of 404 Not Found. This is done to align with your specific requirement.

### **Error Handling**
**404 Not Found:** Returned when a requested resource is not found.
**409 Conflict:** Returned when there is a conflict, such as a duplicate username.

##### **Contributing**
Contributions are **welcome!** If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.

