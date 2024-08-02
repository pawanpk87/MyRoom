## :star2: About the Project
MyRoom is a Hotel management web application.

# 🛠️ High Level Design
![Screenshot 2024-08-02 122824](https://github.com/user-attachments/assets/26f7e20a-773f-4907-a274-400195f6f520)

https://github.com/pawanpk87/MyRoom/assets/87040096/ad4b4ebe-26a0-4051-b3dc-e6080c69d7d5

<!-- Screenshots -->

### :camera: Screenshots(MyRoom dashboard)
<img width="1511" alt="Screenshot 2024-02-27 at 11 49 04 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/375a3071-8442-41c1-8a00-2a71d588a1c7">
<img width="1511" alt="Screenshot 2024-02-28 at 10 01 13 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/a61b1b8f-6094-4a22-9575-ed62245530db">
<img width="1511" alt="Screenshot 2024-02-28 at 10 01 27 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/4f2f425d-1066-45ec-a559-c5ef49adab63">

### :camera: Screenshots(MyRoom client)
<img width="1511" alt="Screenshot 2024-02-28 at 10 03 28 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/26c4a823-de24-4b9d-87d7-1a60cfe49fe1">
<img width="1511" alt="Screenshot 2024-02-28 at 10 03 52 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/86d495ef-3f3e-4fd4-b066-8ed29ae8b80c">
<img width="1511" alt="Screenshot 2024-02-28 at 10 04 53 AM" src="https://github.com/pawanpk87/MyRoom/assets/87040096/f29d2314-1a1d-4dab-8b3d-168db1d5a7a6">


### :space_invader: Tech Stack

<div align="center">
    <table>
        <tr>
            <td>Spring Boot<img width="40" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></td>
            <td>Node.js<img width="40" src="https://user-images.githubusercontent.com/25181517/183568594-85e280a7-0d7e-4d1a-9028-c8c2209e073c.png" alt="Node.js" title="Node.js"/></td>
            <td>Express<img width="40" src="https://user-images.githubusercontent.com/25181517/183859966-a3462d8d-1bc7-4880-b353-e2cbed900ed6.png" alt="Express" title="Express"/></td>
            <td>React<img width="40" src="https://user-images.githubusercontent.com/25181517/183897015-94a058a6-b86e-4e42-a37f-bf92061753e5.png" alt="React" title="React"/></td>
            <td>Next.js<img width="40" src="https://github.com/marwin1991/profile-technology-icons/assets/136815194/5f8c622c-c217-4649-b0a9-7e0ee24bd704" alt="Next.js" title="Next.js"/></td>
            <td>Firebase<img width="40" src="https://user-images.githubusercontent.com/25181517/189716855-2c69ca7a-5149-4647-936d-780610911353.png" alt="Firebase" title="Firebase"/></td>
            <td>TypeScript<img width="40" src="https://user-images.githubusercontent.com/25181517/183890598-19a0ac2d-e88a-4005-a8df-1ee36782fde1.png" alt="TypeScript" title="TypeScript"/></td>
        </tr>
        <tr>
            <td>MySQL<img width="40" src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" alt="MySQL" title="MySQL"/></td>
            <td>MongoDB<img width="40" src="https://user-images.githubusercontent.com/25181517/182884177-d48a8579-2cd0-447a-b9a6-ffc7cb02560e.png" alt="mongoDB" title="mongoDB"/></td>
            <td>Kafka<img width="40" src="https://user-images.githubusercontent.com/25181517/192107004-2d2fff80-d207-4916-8a3e-130fee5ee495.png" alt="kafka" title="kafka"/></td>
            <td>Swagger<img width="40" src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" alt="Swagger" title="Swagger"/></td>
        </tr>
    </table>
</div>


# 🏃‍♀️ Running

-   First, start the **discovery server**.
-   Run **Kafka**:
    ```
    Kafka with ZooKeeper	
    Run the following commands in order to start all services in the correct order:
    # Start the ZooKeeper service $ bin/zookeeper-server-start.sh config/zookeeper.properties
    Open another terminal session and run:
    # Start the Kafka broker service $ bin/kafka-server-start.sh config/server.properties
    ```
-   Start the **auth server** [Auth Server](https://github.com/pawanpk87/MyRoom/tree/main/auth-server#how-to-run-auth-server).
-   Start the **MyRoom gateway**.
-   Then run all the services one by one: **booking service**, **onboarding server**, **organization pay service**, **organization service**, **payment server**, **review service**, **room service**, **mail service**.
-   Run both frontend apps: **MyRoom client**, **MyRoom Admin client**.
-   Use **Stripe CLI** to trigger Stripe webhook events. Refer to [Stripe Documentation](https://docs.stripe.com/payments/handling-payment-events#use-cli).




<!-- CONTACT -->
## Contact

Pawan Kumar Mehta - arowpk@gmail.com

Project Link: [https://github.com/pawanpk87/MyRoom](https://github.com/pawanpk87/MyRoom)

