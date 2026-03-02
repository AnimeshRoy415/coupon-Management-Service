Here’s an updated version of the README that includes the new analytics features:

---
# Coupon Management System

## Overview

The Coupon Management System is a Spring Boot application designed to manage discount coupons for an e-commerce platform. It allows users to create, update, retrieve, and delete coupons, apply them to shopping carts, and analyze their performance. Additionally, the system supports dynamic coupon generation, user segmentation, and other advanced features to enhance the user experience.

## Features

- **Create Coupons**: Easily create new discount coupons with specific criteria.
- **Retrieve Coupons**: Fetch individual coupons or all available coupons, with optional filtering by active/inactive status.
- **Update Coupons**: Update existing coupons with new details.
- **Delete Coupons**: Remove coupons from the system.
- **Apply Coupons**: Apply one or multiple coupons to a shopping cart and calculate discounts.
- **Find Applicable Coupons**: Retrieve a list of applicable coupons for a specific cart.
- **Analytics**:
  - **Top Performing Coupons**: Identify the top-performing coupons based on the revenue they generate.
  - **Least Used Coupons**: Identify the coupons with the least number of redemptions.
  - **Performance Reports**: Generate detailed reports on coupon performance and usage trends.
  - **Usage History**: Track and retrieve the usage history of coupons.
- **Coupon Expiry Notifications**: Notify users when a coupon is about to expire.
- **Coupon Stacking Rules**: Define rules for stacking multiple coupons in a purchase.
- **Fraud Detection**: Evaluate users for potential coupon abuse using a fraud scoring system.
- **Auto Reactivation of Blocked Coupons**: Coupons blocked for misuse are automatically unblocked after a configurable time period (e.g., 24 hours).
- **Auto Expiry of Coupons**: Coupons are automatically marked expired after their end date using a scheduled job.
- **Planned Features**
  - **Event-Triggered Coupons**: Trigger coupons for birthdays, anniversaries, festivals (e.g., Puja), or special events.
  - **Auto-apply Best Coupon**: Automatically apply the most beneficial coupon(s) to a cart.
  - **Multi-Coupon Strategies**: Apply multiple coupons with stacking and prioritization rules.

## Getting Started

### Prerequisites

Ensure you have the following installed before running the application:

- **Java 17** or higher
- **Maven 3.6.3** or higher
- **Spring Boot 3.x**

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AnimeshRoy415/CouponManagement.git
   cd coupon-management-system
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

### Configuration

The application uses an in-memory H2 database by default. You can configure a different database in the `application.properties` file.

### Running Tests

To run unit tests:

```bash
mvn test
```

## API Endpoints

The application exposes several RESTful endpoints for managing, applying, and analyzing coupons.

### Coupon Management

- **Create Coupon**
  - `POST /api/v1/coupons`
  - Request Body: `CouponDto`
  - Response: `Coupon`

- **Get Coupon by ID**
  - `GET /api/v1/coupons/{id}`
  - Response: `Coupon`

- **Get All Coupons**
  - `GET /api/v1/coupons`
  - Query Params: `isActive` (optional)
  - Response: `List<Coupon>`

- **Update Coupon**
  - `PUT /api/v1/coupons/{id}`
  - Request Body: `Coupon`
  - Response: `Coupon`

- **Delete Coupon**
  - `DELETE /api/v1/coupons/{id}`

- **Find Applicable Coupons**
  - `POST /api/v1/coupons/applicable`
  - Request Body: `Cart`
  - Response: `List<CouponResponseDto>`

- **Apply Multiple Coupons**
  - `POST /api/v1/coupons/apply`
  - Request Body: `ApplyCouponsRequest`
  - Response: `Cart`

### Coupon Analytics

- **Get Top Performing Coupons**
  - `GET /api/v1/coupons/top-performing`
  - Query Params: `limit` (default is 10)
  - Response: `List<Coupon>`

- **Get Least Used Coupons**
  - `GET /api/v1/coupons/least-used`
  - Query Params: `limit` (default is 10)
  - Response: `List<Coupon>`

- **Generate Performance Report**
  - `POST /api/v1/coupons/generate-performance-report`
  - Action: Generates a report on coupon performance.

- **Generate Usage Report**
  - `POST /api/v1/coupons/generate-usage-report`
  - Action: Generates a report on coupon usage.

### Coupon Usage History

- **Get All Coupon Usage History**
  - `GET /api/v1/coupons/history`
  - Response: `List<CouponUsageHistResponseDto>`

- **Get Coupon Usage History by ID**
  - `GET /api/v1/coupons/history/{id}`
  - Response: `CouponUsageHistResponseDto`

- **Get Coupon Usage History List by IDs**
  - `GET /api/v1/coupons/history/by-ids`
  - Query Params: `ids`
  - Response: `List<CouponUsageHistResponseDto>`

### Advanced Features

- **Coupon Expiry Notifications**
  - `GET /api/coupons/notify-expiry`
  - Action: Triggers notifications for coupons about to expire.
- **Fraud Detection**
  - `GET /api/v1/fraud/{userId}`
  - Action: Evaluates the fraud risk score of a specific user.

### Example Usage

1. **Creating a Coupon:**

   Request:

   ```json
   {
    "type": "CART_WISE",
    "code": "FAST50",
    "validationDays": 10,
    "repetitionLimit": 100,
    "maxUsagePerUser": 100,
    "details": {
      "threshold": 100,
      "discountPercentage": 50,
      "maxDiscountAmount": 5000
      }
    }
   ```

   Response:

   ```json
   {
    "success": true,
    "status": 201,
    "message": "Coupon created successfully",
    "data": {
        "id": 11,
        "type": "cart-wise",
        "details": {
            "buyProducts": null,
            "discountPercentage": 50.0,
            "getProducts": null,
            "maxDiscountAmount": 5000.0,
            "productId": null,
            "threshold": 100.0
        },
        "code": "FAST50",
        "expirationDate": 1773322339081,
        "repetitionLimit": 100,
        "active": true,
        "redemptionCount": 0,
        "totalRevenueGenerated": 0.0,
        "stackable": true,
        "maxUsagePerUser": 100,
        "available": true,
        "expired": false
      }
    }
   ```

2. **Applying Multiple Coupons:**

   Request:

   ```json
     {
      "couponIds": [5, 7, 8],
      "cart": {
        "id": 101,
        "userId": 5001,
        "items": [
          {
            "productId": 11,
            "productName": "iPhone 15",
            "price": 80000,
            "quantity": 1
          }
        ],
        "userId": 113,
        "totalAmount": 80000
      }
    }
   ```

   Response:

   ```json
   {
    "success": true,
    "status": 200,
    "message": "Coupons applied successfully",
    "data": {
        "userId": 113,
        "items": [
            {
                "productId": 11,
                "quantity": 1,
                "price": 80000.0,
                "totalDiscount": 20000.0
            }
        ],
        "totalPrice": 80000.0,
        "totalDiscount": 40000.0,
        "finalPrice": 40000.0,
        "appliedCoupons": [
            {
                "id": 5,
                "type": "product-wise",
                "details": {
                    "buyProducts": [],
                    "discountPercentage": 20.0,
                    "getProducts": [],
                    "maxDiscountAmount": null,
                    "productId": 11,
                    "threshold": null
                },
                "code": "IPHONE20",
                "expirationDate": 1773650685102,
                "repetitionLimit": 1,
                "active": false,
                "redemptionCount": 5,
                "totalRevenueGenerated": 80000.0,
                "stackable": false,
                "maxUsagePerUser": null,
                "available": false,
                "expired": false
            },
            {
                "id": 7,
                "type": "cart-wise",
                "details": {
                    "buyProducts": [],
                    "discountPercentage": 15.0,
                    "getProducts": [],
                    "maxDiscountAmount": 150.0,
                    "productId": null,
                    "threshold": 1000.0
                },
                "code": "CART15",
                "expirationDate": 1773308034565,
                "repetitionLimit": 1,
                "active": false,
                "redemptionCount": 3,
                "totalRevenueGenerated": 450.0,
                "stackable": false,
                "maxUsagePerUser": 2,
                "available": false,
                "expired": false
            },
            {
                "id": 8,
                "type": "product-wise",
                "details": {
                    "buyProducts": [],
                    "discountPercentage": 25.0,
                    "getProducts": [],
                    "maxDiscountAmount": null,
                    "productId": 11,
                    "threshold": null
                },
                "code": "IPHONE25",
                "expirationDate": 1773740067334,
                "repetitionLimit": 1,
                "active": false,
                "redemptionCount": 3,
                "totalRevenueGenerated": 60000.0,
                "stackable": false,
                "maxUsagePerUser": 1,
                "available": false,
                "expired": false
            }
        ]
      }
    }
   ```


## Exception Handling

The application includes custom exceptions for specific scenarios:

- **CouponNotFound:** Thrown when a requested coupon does not exist.
- **CouponExpire:** Thrown when a coupon is expired.
- **ConditionNotMeet:** Thrown when a coupon's conditions are not met during application.
- **CouponUsageHistoryNotFound:** Thrown when requested couponUsageHistory does not exist.
- **InvalidSegmentCriteria:** Thrown when the user segmentation criteria are invalid.

## Database Schema

The application uses an H2 in-memory database with the following schema:

- **Coupons Table:**
  - `id`: Long (Primary Key)
  - `type`: Enum (Coupon type, e.g., CART_WISE, PRODUCT_WISE, BX_GY)
  - `details`: JSON (Details about the coupon)
  - `expirationDate`: Long (Timestamp)
  - `repetitionLimit`: Integer
  - `totalRevenueGenerated`: Double (Total revenue generated by the coupon)
  - `redemptionCount`: Integer (Number of times the coupon has been redeemed)
  - `isShared`: Boolean (Indicates if the coupon has been shared)

- **Cart Table:**
  - `id`: Long (Primary Key)
  - `items`: List<CartItem> (One-to-Many relationship with CartItem)

- **CouponUsageHistory Table:**
  - `id`: Long (Primary Key)
  - `couponId`: Long (Foreign Key)
  - `userId`: Long
  - `cart`: JSON
  - `usedAt`: LocalDateTime
  - `LocalDateTime`: discountApplied

## Postman Collection
  - You can test all endpoints with Postman:
  ```JSON
      {
  "info": {
    "_postman_id": "33890f62-8f26-41b6-bd2f-4f3285f336a9",
    "name": "Coupon Management API",
    "description": "Coupon Management System APIs with base_url variable",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
    "_exporter_id": "22595976"
  },
  "item": [
    {
      "name": "Create CartWise Coupon",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"code\": \"CART10\",\n  \"type\": \"CART_WISE\",\n  \"discountType\": \"PERCENTAGE\",\n  \"discountValue\": 10,\n  \"minCartValue\": 1000,\n  \"maxDiscount\": 500,\n  \"startDate\": \"2026-03-01\",\n  \"endDate\": \"2026-03-31\",\n  \"usageLimit\": 100,\n  \"active\": true\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Create ProductWise Coupon",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"code\": \"PROD20\",\n  \"type\": \"PRODUCT_WISE\",\n  \"discountType\": \"PERCENTAGE\",\n  \"discountValue\": 20,\n  \"productIds\": [1,2],\n  \"minQuantity\": 1,\n  \"startDate\": \"2026-03-01\",\n  \"endDate\": \"2026-03-31\",\n  \"usageLimit\": 50,\n  \"active\": true\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Create BX_GY Coupon",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"code\": \"BUY2GET1\",\n  \"type\": \"BX_GY\",\n  \"buyQuantity\": 2,\n  \"getQuantity\": 1,\n  \"productIds\": [1],\n  \"startDate\": \"2026-03-01\",\n  \"endDate\": \"2026-03-31\",\n  \"usageLimit\": 100,\n  \"active\": true\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get Coupon By ID",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get All Coupons",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons?active=true",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons"
          ],
          "query": [
            {
              "key": "active",
              "value": "true"
            }
          ]
        }
      },
      "response": []
    },
    {
      "name": "update Cart wise Coupon",
      "request": {
        "method": "PUT",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"type\": \"CART_WISE\",\n  \"code\": \"CART15\",\n  \"expirationDate\": 1755000000000,\n  \"repetitionLimit\": 1,\n  \"details\": {\n    \"threshold\": 1500,\n    \"discountPercentage\": 15,\n    \"maxDiscountAmount\": 300\n  },\n  \"redemptionCount\": 0,\n  \"totalRevenueGenerated\": 0.0\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Update Product wise Coupon",
      "request": {
        "method": "PUT",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"code\": \"CART15\",\n  \"discountValue\": 15,\n  \"active\": true\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Update Bx_Gy Coupon",
      "request": {
        "method": "PUT",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"type\": \"BX_GY\",\n  \"code\": \"BUY3GET1\",\n  \"expirationDate\": 1755000000000,\n  \"repetitionLimit\": 3,\n  \"details\": {\n    \"buyProducts\": [\n      {\n        \"productId\": 101,\n        \"quantity\": 3,\n        \"price\": 500\n      }\n    ],\n    \"getProducts\": [\n      {\n        \"productId\": 102,\n        \"quantity\": 1,\n        \"price\": 300\n      }\n    ],\n    \"discountPercentage\": 50,\n    \"maxDiscountAmount\": 500\n  },\n  \"redemptionCount\": 0,\n  \"totalRevenueGenerated\": 0.0\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Delete Coupon",
      "request": {
        "method": "DELETE",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Find Applicable Coupons",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"cartTotal\": 2000,\n  \"items\": [\n    { \"productId\": 1, \"quantity\": 2, \"price\": 500 },\n    { \"productId\": 2, \"quantity\": 1, \"price\": 1000 }\n  ]\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/applicable",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "applicable"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Apply Multiple Coupons cart wise",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"couponIds\": [1,2],\n  \"cart\": {\n    \"cartTotal\": 2000,\n    \"items\": [\n      { \"productId\": 1, \"quantity\": 2, \"price\": 500 }\n    ]\n  }\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/apply",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "apply"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Apply Multiple Coupons Product wise",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"couponIds\": [1,2],\n  \"cart\": {\n    \"cartTotal\": 2000,\n    \"items\": [\n      { \"productId\": 1, \"quantity\": 2, \"price\": 500 }\n    ]\n  }\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/apply",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "apply"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Apply Multiple Coupons Bx_Gy",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"couponIds\": [1,2],\n  \"cart\": {\n    \"cartTotal\": 2000,\n    \"items\": [\n      { \"productId\": 1, \"quantity\": 2, \"price\": 500 }\n    ]\n  }\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/apply",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "apply"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Top Performing Coupons",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/analytics/top-performing?limit=10",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "analytics",
            "top-performing"
          ],
          "query": [
            {
              "key": "limit",
              "value": "10"
            }
          ]
        }
      },
      "response": []
    },
    {
      "name": "Least Used Coupons",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/analytics/least-used?limit=10",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "analytics",
            "least-used"
          ],
          "query": [
            {
              "key": "limit",
              "value": "10"
            }
          ]
        }
      },
      "response": []
    },
    {
      "name": "Expiring Soon Coupons",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/analytics/expiring-soon?days=7",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "analytics",
            "expiring-soon"
          ],
          "query": [
            {
              "key": "days",
              "value": "7"
            }
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get All Coupon Usage History",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/history",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "history"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get Coupon Usage History By ID",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/history/1",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "history",
            "1"
          ]
        }
      },
      "response": []
    },
    {
      "name": "Get Coupon Usage History By IDs",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/v1/coupons/history/by-ids?ids=1&ids=2",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "coupons",
            "history",
            "by-ids"
          ],
          "query": [
            {
              "key": "ids",
              "value": "1"
            },
            {
              "key": "ids",
              "value": "2"
            }
          ]
        }
      },
      "response": []
    },
    {
      "name": "Fraud checker by userId",
      "protocolProfileBehavior": {
        "disableBodyPruning": true
      },
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"couponIds\": [1,2],\n  \"cart\": {\n    \"cartTotal\": 2000,\n    \"items\": [\n      { \"productId\": 1, \"quantity\": 2, \"price\": 500 }\n    ]\n  }\n}"
        },
        "url": {
          "raw": "{{base_url}}/api/v1/fraud/101",
          "host": [
            "{{base_url}}"
          ],
          "path": [
            "api",
            "v1",
            "fraud",
            "101"
          ]
        }
      },
      "response": []
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8088"
    }
  ]
}
```

## Contributing

Contributions are welcome! Fork the repository and create a pull request for any feature additions or bug fixes.

---

This README now reflects an end-to-end implementation with all the requested features. Would you like to dive into the implementation details of any specific feature?
