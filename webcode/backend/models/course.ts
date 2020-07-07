import mongoose = require('mongoose');
import {IResource} from './resource';
import {ISection, sectionSchema} from './section';


export interface ICourse extends mongoose.Document {
  title: string;
  image: string;
  category: string;
  description: string;
  sections: ISection[];
  rating: number;
  price: number;
//  author: User;
}

export const CourseSchema = new mongoose.Schema({
  title: {type: String, required: true, trim: true},
  image: {type: String, required: true, trim: true},
  category: {type: String, required: true, trim: true},
  description: {type: String, required: true, trim: true},
  rating: {type: Number, required: true},
  duration: {type: Number, required: false},
  price: {type: Number, required: true},
  sections: {type: [sectionSchema], required: true}
});

export const CourseModel = mongoose.model('Course', CourseSchema);
