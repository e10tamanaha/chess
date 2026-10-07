# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Sequence Diagram

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygRUQHPCMykoKp+h-Ds0KPHCFRvl2CBCeKGKCcJBJEmApIqsGGr6TA0AwGpOwwDM2SqOK9joVAAA8SaWbUereJsMAAEIho5ahgFGMZxopInIOY4WlKBtTpvhoxjDmqh5vM0FFiW9R6OuKKEslDb0duKAVMO-K1IyLJsvpc40vuFTLmKEpujKcplu8SrNTlbX0iN2iebAPZ9pgrnqjAaAQONaxeT5AJGBAajrRAzDHGAIDxCFOWRTA0UQLFzLTJe0BIAAXigHCpSgsYKUUWUYFd1TPOmACMBElWVBZjJV0D1D4j16s9b27HRTaWa1t7tYecgoM+8QYrj14zaoA3Co+AaXh6OlhRFzpdvZ4oZKoAGXUmVO1OB5bEaR3wUVR9YkahDagZhf1gDheEEfpNFkWMvOIfz3MowxnjeH4-heCg6AxHEiQa1r9m+FgomCnljTSBG-ERu0EbdD0cmBQUwxy0hIXaWZzzO+gmBUxZtO1NZ9iG+el582gpJo+URMdUyuPB-B8thzefUjiTorik+FPzWNkAu6tGoE-N2Trb2-aWdNGP0ktpe00m5R5bd93w-EiPvZ933xlpFSiflTig0V4P5hVxYwwqTct8jjYMUTteRQX8jNejyftRwKDcMel5x5RCe9byKflMuK9r4Yc-AAvbuws8VAQEgDE+77El1CMmmA1Atfd7hhVP5PjGqwEKLrv4bA4oNT8TRDAAA4kqDQxtxI1HqA0cBVtbb2CVE7EOCdXblDZqrdEN1NrIByDAeI7soDexIffKgSICFgEgTmTeodw410jhXUcnVY6e0TtPfewohoZxfFnGsGC87k34fIGARcq5TSTFHSRaMKHPAbpKMeUBXqt2jF9dKrsu6ix7n3bM-IIZDyqqPCi49GpNmnhQpEJ8F7MKXvSahtC1AYh3guYm3DagUG4OASsCAIFKg9JYuufs4AQD7CgcAClQpOJ5OUM+WCSG1FATkJxqhGbMzvsEh+7NlgoJzAWBo4w8koAAJLSALMDcIwRAggk2PEXUKA3Scj2N8ZIoA1RNMgosb4xSABySpukXBgJ0Z+D8346I-lmXJUCClFKVGUipVSanLDqQ0zpnxukgjaSADpRFypjB6UqfpcxBnDKVj-Zi-gOAAHY3BOBQE4GIEZghwC4gANngBOQwTiYC-RKGJX0L94GtA6Mg1BTdQ5Zj6UqUZiZKhs2NOgpCaw5lzGOfstCVMTZ+xgFjdETj6EJzWHAL5TikokmaoKKOMA2Ebw4a4-c95hQwF4SIq8AjEXxxdkTECOKT4LWLstYR-KAC8IrBXV07Ly7JSjTEqKRm3TRwta7wB0TAEGYMDGD0LMPUscM5WqInk1YR0K5h+QCkFSlKrTYJQ4OSlK6j26FGVcmNVBVNW5m1VDXV1UQwwDqk5cxDFLJUpYfUPFKACWmq5EnXeDolzMrJjE+aGI0AoFisUspaw02xTgPEfkTiGV3mtTizN0grUfkSZ8o8kalTpIQIBEh0q4EwFRaU8p9RKnVNhRhFV3cYCTKKmWxZ3bv4q0uZYVe1lYqxCQAkMAk6+x3RgAAKWvmgfxZr-DbLVH88w2KZXNGZDJHoxS0FcvQFmbACBgCTqgKE6yUA1hlp7fCeFVaOEop+Deu9D7oB7AAOosBKdbHocV+IKDgAAaUOXMBZnalmmQvm+jslCXQwAAFbroJZ+1d2G60oHqhSkNu57GjhpTHOlSL0BFvjZUZcLL05stGvKDhsa3HNqRMx+aXlJHCszmIsVEqK2AplQ9A1CrHVKpfiLf5aZe4etKl66GerlGGqDStVUGpr23soH+xax0YBAZAxGMDEHoMwBaGkGl8zpDmvkg4ETqH-S2vtYqn6LrKj9vdf3LV5UdXGJqv6wjgbzkkapGGijNClQYjLbRxc9HmWsuTWIsaZb2OMpLZ2eoKXgACr41pzdKAZRCdkbTZtLnEoheSu5juMm+1uolr5z1-nvWBb9QGhq5zhGddxOuEAYSvImjNPKYhAo8v6Sc6bQMI2wxIVq86+rrq5PqozIpwxAWR7DeDOaG64ZkJhfK6RuN9IFTYC0PimLxTCYsKZfUDIOnvS5acwijEV8b6kixVlltX9POqpW4Or+TVx1qy8Le7Wc7dZQHB4gYMsBgDYGvYQPI8YYGiZbQ0c2ltra22MCFQUr2NqyggOaT7TbYFcZANwPALirURbI7DGcCgWguCp3D2nPKE33aOBAGgviNzui3OFmQkXrvM9Z9TqAHPbtc5gBkGYvPDAmj8c94X1KKLSHF2zmn8WBSy-l4r-nNjhHsHlETBbMBzyI6ygKSAMAUA1AtJwbA67nIZeLbA-05upMeaW15tVABWJr+iWuQxU-UDEK9VDjmIOlTyAAze3uriQaZI5W5D9R3u33J57sCr7ZOpgHcH85QA