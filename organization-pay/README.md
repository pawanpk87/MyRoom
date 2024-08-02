## How to run organization-pay service
1. Create Stripe account [https://dashboard.stripe.com](https://dashboard.stripe.com).

2. Update the secret-key in application.yml. (Get the secret-key from here) [https://dashboard.stripe.com/test/apikeys](https://dashboard.stripe.com/test/apikeys)

3. Do not do this process now but when all services are running then trigger stripe event [https://docs.stripe.com/payments/handling-payment-events#use-cli](https://docs.stripe.com/payments/handling-payment-events#use-cli)
    replace endpointSecret in application.yml.

4. Then run the project
   Access the Swagger UI at [http://localhost:8087/swagger-ui/index.html](http://localhost:8087/swagger-ui/index.html).
