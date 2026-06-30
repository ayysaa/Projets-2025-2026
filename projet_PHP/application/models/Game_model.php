<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Game_model extends CI_Model {

    public function __construct() {
        parent::__construct();
        $this->load->database();
    }

    public function get_all_games($order_by = 'name', $direction = 'ASC') {
        if (!in_array($order_by, ['name', 'releaseYear'])) {
            $order_by = 'name';
        }
        
        $this->db->order_by($order_by, $direction);
        $query = $this->db->get('game');
        return $query->result_array();
    }
    
    // Récupérer les infos de base d'un jeu
    public function get_game($id) {
        $this->db->where('id', $id);
        $query = $this->db->get('game');
        return $query->row_array();
    }

    public function get_game_categories($game_id) {
        $this->db->select('category.id, category.description');
        $this->db->from('category');
        $this->db->join('game_category', 'game_category.categoryId = category.id');
        $this->db->where('game_category.gameId', $game_id);
        $query = $this->db->get();
        return $query->result_array();
    }

    public function get_game_genres($game_id) {
        $this->db->select('genre.id, genre.description');
        $this->db->from('genre');
        $this->db->join('game_genre', 'game_genre.genreId = genre.id');
        $this->db->where('game_genre.gameId', $game_id);
        $query = $this->db->get();
        return $query->result_array();
    }

    public function search_games($keyword, $order_by = 'name') {
        if (!in_array($order_by, ['name', 'releaseYear'])) {
            $order_by = 'name';
        }
        $this->db->group_start();
        $this->db->like('name', $keyword);
        $this->db->or_like('shortDescription', $keyword);
        $this->db->group_end();
        $this->db->order_by($order_by, 'ASC');
        $query = $this->db->get('game');
        return $query->result_array();
    }

    public function get_all_categories() {
        $query = $this->db->get('category');
        return $query->result_array();
    }

    public function get_all_genres() {
        $query = $this->db->get('genre');
        return $query->result_array();
    }

    public function get_category($id) {
        $this->db->where('id', $id);
        $query = $this->db->get('category');
        return $query->row_array();
    }

    public function get_genre($id) {
        $this->db->where('id', $id);
        $query = $this->db->get('genre');
        return $query->row_array();
    }

    public function get_games_by_category($category_id) {
        $this->db->select('game.*');
        $this->db->from('game');
        $this->db->join('game_category', 'game_category.gameId = game.id');
        $this->db->where('game_category.categoryId', $category_id);
        $query = $this->db->get();
        return $query->result_array();
    }

    public function get_games_by_genre($genre_id) {
        $this->db->select('game.*');
        $this->db->from('game');
        $this->db->join('game_genre', 'game_genre.gameId = game.id');
        $this->db->where('game_genre.genreId', $genre_id);
        $query = $this->db->get();
        return $query->result_array();
    }

    public function insert_game($data) {
        return $this->db->insert('game', $data);
    }

    public function update_game($id, $data) {
        $this->db->where('id', $id);
        return $this->db->update('game', $data);
    }

    public function delete_game($id) {
        $this->db->where('gameId', $id);
        $this->db->delete('game_category');
        
        $this->db->where('gameId', $id);
        $this->db->delete('game_genre');

        $this->db->where('id', $id);
        return $this->db->delete('game');
    }
}