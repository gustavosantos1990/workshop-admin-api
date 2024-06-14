# Shop Admin API



### Features

- Components
    - find all components - GET /v1/components
    - find component by id - GET /v1/components/{id}
    - create new component - POST /v1/components
    - update component - PATCH /v1/components/{id}
    - delete component - DELETE /v1/components/{id}

- Products
    - find all products - GET /v1/products
    - create new product - POST /v1/products
    - update product -
    - find product components - GET /v1/products/{id}/components
    - add product component - POST /v1/products/{id}/components

- Requests
    - find all requests - GET /v1/requests
    - create new request - POST /v1/requests



### Alternatives

1. Once a product has it's components finished and get ready to be requested those components can't be updated. \
   Every time the component need to be updated (include, update, remove) a new version of the product is created.
2. Every request has it's own copy of product components, so once a product is included in a request it doesn't matter if any of the components is being updated. \
   Each product can be 'refreshed' after included to get the new updates.
3. Don't keep track of components updates.

Environment Variables
- API_PORT=
- DB_HOST=
- DB_NAME=
- DB_PASSWORD=
- DB_PORT=
- DB_USERNAME=