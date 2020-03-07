import { writable } from 'svelte/store';

export const remaining_time = writable(10);
export const time_progress = writable(100);
export const opponent_score = writable(0);
export const my_score = writable(0);
export const round_number = writable(1);