import { nutrients } from "./nutrients.model";
import { ingredients } from "./ingredients.model";

export class recipe {
    author: string;
    calories: number;
    description: string;
    image: string;
    ingredients: Array<ingredients>;
    label: string;
    nutrients: nutrients;
    rating: number;
    recipeId: number;
    username: string;
    servings: number;
    totalTime: number;
    url: string;

}