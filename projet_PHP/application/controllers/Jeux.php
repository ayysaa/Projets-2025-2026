<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Jeux extends CI_Controller {

    public function __construct() {
        parent::__construct();
        $this->load->model('game_model');
        $this->load->helper('url');
        $this->load->helper('form');
        $this->load->library('form_validation');
    }

    public function index($tri = 'name') {
        $data['title'] = "Liste des jeux";
        $data['tri_actuel'] = $tri;
        
        $search = $this->input->get('search');
        $data['search'] = $search;

        if (!empty($search)) {
            $data['games'] = $this->game_model->search_games($search, $tri);
        } else {
            $data['games'] = $this->game_model->get_all_games($tri, 'ASC');
        }

        $this->load->view('header', $data);
        $this->load->view('jeux_liste', $data);
        $this->load->view('footer');
    }

    public function voir($id = NULL) {
        if ($id === NULL) {
            redirect('jeux');
        }

        $data['game'] = $this->game_model->get_game($id);

        if (empty($data['game'])) {
            show_404();
        }

        $data['game']['price'] = $data['game']['price'] / 100;

        $data['categories'] = $this->game_model->get_game_categories($id);
        $data['genres'] = $this->game_model->get_game_genres($id);
        $data['title'] = $data['game']['name'];

        $this->load->view('header', $data);
        $this->load->view('jeux_detail', $data);
        $this->load->view('footer');
    }

    public function categories_genres() {
        $data['title'] = "Catégories et Genres";
        $data['categories'] = $this->game_model->get_all_categories();
        $data['genres'] = $this->game_model->get_all_genres();

        $this->load->view('header', $data);
        $this->load->view('index', $data);
        $this->load->view('footer');
    }

    public function categorie($id = NULL) {
        if ($id === NULL) {
            redirect('jeux/categories_genres');
        }
        $category = $this->game_model->get_category($id);
        if (empty($category)) {
            show_404();
        }
        $data['title'] = "Catégorie : " . $category['description'];
        $data['category'] = $category;
        $data['games'] = $this->game_model->get_games_by_category($id);

        $this->load->view('header', $data);
        $this->load->view('categorie_detail', $data);
        $this->load->view('footer');
    }

    public function genre($id = NULL) {
        if ($id === NULL) {
            redirect('jeux/categories_genres');
        }
        $genre = $this->game_model->get_genre($id);
        if (empty($genre)) {
            show_404();
        }
        $data['title'] = "Genre : " . $genre['description'];
        $data['genre'] = $genre;
        $data['games'] = $this->game_model->get_games_by_genre($id);

        $this->load->view('header', $data);
        $this->load->view('genre_detail', $data);
        $this->load->view('footer');
    }

    public function creer() {
        $data['title'] = "Ajouter un jeu";
        
        $this->form_validation->set_rules('name', 'Nom', 'required');
        $this->form_validation->set_rules('releaseYear', 'Année', 'required|numeric');
        $this->form_validation->set_rules('price', 'Prix', 'required|numeric');

        if ($this->form_validation->run() === FALSE) {
            $this->load->view('header', $data);
            $this->load->view('jeux_form', $data);
            $this->load->view('footer');
        } else {
            $save_data = [
                'name' => $this->input->post('name'),
                'releaseYear' => $this->input->post('releaseYear'),
                'shortDescription' => $this->input->post('shortDescription'),
                'price' => $this->input->post('price') * 100,
                'metacritic' => $this->input->post('metacritic') ? $this->input->post('metacritic') : NULL
            ];
            $this->game_model->insert_game($save_data);
            redirect('jeux');
        }
    }

    public function editer($id = NULL) {
        if ($id === NULL) {
            redirect('jeux');
        }
        
        $data['game'] = $this->game_model->get_game($id);
        if (empty($data['game'])) {
            show_404();
        }

        $data['game']['price'] = $data['game']['price'] / 100;

        $data['title'] = "Modifier : " . $data['game']['name'];

        $this->form_validation->set_rules('name', 'Nom', 'required');
        $this->form_validation->set_rules('releaseYear', 'Année', 'required|numeric');
        $this->form_validation->set_rules('price', 'Prix', 'required|numeric');

        if ($this->form_validation->run() === FALSE) {
            $this->load->view('header', $data);
            $this->load->view('jeux_form', $data);
            $this->load->view('footer');
        } else {
            $update_data = [
                'name' => $this->input->post('name'),
                'releaseYear' => $this->input->post('releaseYear'),
                'shortDescription' => $this->input->post('shortDescription'),
                'price' => $this->input->post('price') * 100,
                'metacritic' => $this->input->post('metacritic') ? $this->input->post('metacritic') : NULL
            ];
            $this->game_model->update_game($id, $update_data);
            redirect('jeux/voir/' . $id);
        }
    }

    public function supprimer($id = NULL) {
        if ($id === NULL) {
            redirect('jeux');
        }
        $this->game_model->delete_game($id);
        redirect('jeux');
    }
}