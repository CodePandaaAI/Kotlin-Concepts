# 🧠 Kotlin Concepts

> **Learn Kotlin by reading and running real code.** Each file teaches one concept with a short header and runnable `main()` — open, read, run.

---

## 🗺️ How to Use This Repo

1. **Pick a topic** from the table below
2. **Open the file** in IntelliJ IDEA
3. **Read the header comment** — what this file teaches and what to know first
4. **Run `main()`** to see the concepts in action
5. **Experiment!** Change values, break things, learn from errors

---

## 📚 Topics

### `basics/` — Start Here
| File                         | Concept             | You'll Learn                                   |
|------------------------------|---------------------|------------------------------------------------|
| `BasicGenerics.kt`           | Generics            | Type parameters, type safety, code reusability |
| `ExtensionFunctionsBasic.kt` | Extension Functions | Adding methods to existing classes             |

### `functions/` — Functional Programming
| File                      | Concept               | You'll Learn                                                      |
|---------------------------|-----------------------|-------------------------------------------------------------------|
| `Lambdas.kt`              | Lambdas               | Anonymous functions, trailing lambdas, `it`, lambda with receiver |
| `ScopeFunctions.kt`       | Scope Functions       | `let`, `run`, `apply`, `also`, `with` — when to use each          |
| `HigherOrderFunctions.kt` | Higher-Order + Inline | How `filter()`, `map()` work internally, reified generics         |

### `oop/` — Object-Oriented Concepts
| File                  | Concept                     | You'll Learn                                 |
|-----------------------|-----------------------------|----------------------------------------------|
| `AnonymousObjects.kt` | Anonymous Objects           | One-time interface implementations           |
| `SealedClasses.kt`    | Sealed Classes & Interfaces | Exhaustive `when`, sealed class vs interface |

### `generics/` — Deep Dive
| File          | Concept                       | You'll Learn                                               |
|---------------|-------------------------------|------------------------------------------------------------|
| `Variance.kt` | Variance (`out`/`in`/Nothing) | Invariance, covariance, contravariance, the Result pattern |

### `dsl/` — Domain Specific Languages
| File                   | Concept         | You'll Learn                                 |
|------------------------|-----------------|----------------------------------------------|
| `DSLBuilderPattern.kt` | Builder Pattern | Lambda with receiver, nested configuration   |
| `HTMLDSLBuilder.kt`    | HTML Builder    | Type-safe HTML generation                    |
| `KtorStyleDSL.kt`      | Ktor-Style DSL  | Plugin system, real-world framework patterns |
| `DSLWithEnums.kt`      | DSL + Enums     | Type-safe configuration with enums           |
| `WorldBuilderDSL.kt`   | World Builder   | Creative DSL: build a fantasy world!         |

### `patterns/` — Real-World Patterns
| File               | Concept            | You'll Learn                                   |
|--------------------|--------------------|------------------------------------------------|
| `ResultPattern.kt` | Result\<T\>        | Sealed + Generics + Nothing for error handling |
| `LoginSystem.kt`   | Repository Pattern | Interfaces, DI, complete login system          |

### `practice/` — Hands-On Practice
| File             | Concept             | You'll Learn                            |
|------------------|---------------------|-----------------------------------------|
| `SafeApiCall.kt` | Generic API Wrapper | Try-catch + generics + Result in action |

### `coroutines/` — Coming Soon 🚧
Async programming with coroutines, suspend functions, and Flow.

---

## 🎯 Suggested Learning Path

```
basics/ → functions/ → oop/ → generics/ → dsl/ → patterns/ → practice/
```

Each section builds on the previous one. The DSL section especially relies on understanding lambdas and extension functions.

---

## 🤝 Contributing

Want to add a concept? Follow these rules:
1. **One concept per file** — keep it focused
2. **Every file must have `main()`** — runnable out of the box
3. **Header comment** — one concept only: what it is, why it exists, prerequisites (3–8 lines max)
4. **Code is the lesson** — short inline comments only where the line is not obvious
5. **No FAQ walls** — if it needs a separate file, split the concept
6. **Add to the table above** — so others can find your work

---

## 🛠️ Setup

1. Clone the repo
2. Open in IntelliJ IDEA
3. Navigate to any `.kt` file
4. Click the green ▶️ button next to `main()` to run

No special dependencies needed — just Kotlin!

---

*Built with ❤️ while learning Kotlin in public.*
