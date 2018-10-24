import { nutrients } from "./nutrients.model";
import { ingredients } from "./ingredients.model";

export class recipe {
    label: string;
    description: string;
    image: string;
    URL: string;
    servings: number;
    calories: number;
    totalTime: number;
    nutrients: nutrients
    ingredients: Array<ingredients>
}