import mongoose = require('mongoose');
import {CourseSchema, ICourse} from "./course";

export interface IUser extends mongoose.Document {
  email: string;
  username: string;
  password: string;
  courses: ICourse[];
  // tokens: [{ token: string }];
}

const UserSchema: mongoose.Schema = new mongoose.Schema({
  email: {type: String, trim: true, required: true, unique: true},
  username: {type: String, trim: true, required: true},
  password: {type: String, required: true},
  courses: {type: [mongoose.SchemaTypes.ObjectId], ref: 'Course'}
  // tokens: [{ token: { type: String, required: true } }],
});

// Export the model and return your IUser interface
export default mongoose.model<IUser>('User', UserSchema);
