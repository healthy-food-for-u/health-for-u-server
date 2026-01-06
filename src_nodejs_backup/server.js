import app from "./src_nodejs_backup/app.js";
import { connect } from "./src_nodejs_backup/config/mongoDB.js";

const PORT = process.env.PORT || 3000;

connect()
    .then(() => {
        app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
    })
    .catch((err) => {
        console.error("Failed to connect to MongoDB:", err);
    });