package tn.fst.exam_manager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.fst.exam_manager.domain.ProfessorDetails;
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.domain.enumeration.Rank;
import tn.fst.exam_manager.repository.ProfessorDetailsRepository;
import tn.fst.exam_manager.repository.UserRepository;
import tn.fst.exam_manager.service.dto.AdminUserDTO;
import tn.fst.exam_manager.service.dto.ProfessorDetailsDTO;
import tn.fst.exam_manager.service.dto.UserDTO;
import tn.fst.exam_manager.service.mapper.ProfessorDetailsMapper;
import tn.fst.exam_manager.service.mapper.UserMapper;

/**
 * Service Implementation for managing {@link tn.fst.exam_manager.domain.ProfessorDetails}.
 */
@Service
@Transactional
public class ProfessorDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessorDetailsService.class);

    private final ProfessorDetailsRepository professorDetailsRepository;

    private final ProfessorDetailsMapper professorDetailsMapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserService userService;

    public ProfessorDetailsService(
        ProfessorDetailsRepository professorDetailsRepository,
        ProfessorDetailsMapper professorDetailsMapper,
        UserRepository userRepository,
        UserMapper userMapper,
        UserService userService
    ) {
        this.professorDetailsRepository = professorDetailsRepository;
        this.professorDetailsMapper = professorDetailsMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    /**
     * Save a professorDetails.
     *
     * @param professorDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessorDetailsDTO save(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to save ProfessorDetails : {}", professorDetailsDTO);
        ProfessorDetails professorDetails = professorDetailsMapper.toEntity(professorDetailsDTO);
        professorDetails = professorDetailsRepository.save(professorDetails);
        return professorDetailsMapper.toDto(professorDetails);
    }

    /**
     * Update a professorDetails.
     *
     * @param professorDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessorDetailsDTO update(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to update ProfessorDetails : {}", professorDetailsDTO);
        ProfessorDetails professorDetails = professorDetailsMapper.toEntity(professorDetailsDTO);
        professorDetails = professorDetailsRepository.save(professorDetails);
        return professorDetailsMapper.toDto(professorDetails);
    }

    /**
     * Partially update a professorDetails.
     *
     * @param professorDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessorDetailsDTO> partialUpdate(ProfessorDetailsDTO professorDetailsDTO) {
        LOG.debug("Request to partially update ProfessorDetails : {}", professorDetailsDTO);

        return professorDetailsRepository
            .findById(professorDetailsDTO.getId())
            .map(existingProfessorDetails -> {
                professorDetailsMapper.partialUpdate(existingProfessorDetails, professorDetailsDTO);

                return existingProfessorDetails;
            })
            .map(professorDetailsRepository::save)
            .map(professorDetailsMapper::toDto);
    }

    /**
     * Get all the professorDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessorDetailsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProfessorDetails");
        return professorDetailsRepository.findAll(pageable).map(professorDetailsMapper::toDto);
    }

    /**
     * Get one professorDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessorDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessorDetails : {}", id);
        return professorDetailsRepository.findById(id).map(professorDetailsMapper::toDto);
    }

    /**
     * Delete the professorDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessorDetails : {}", id);
        professorDetailsRepository.deleteById(id);
    }

    @Transactional
    public void importProfessors(MultipartFile file) throws IOException {
        List<ProfessorDetailsDTO> professorDetailsList = parseCsv(file);

        for (ProfessorDetailsDTO professorDetail : professorDetailsList) {
            LOG.debug("DTO Inserting: {}", professorDetailsList);
            String login = professorDetail.getUser().getLogin();
            String email = professorDetail.getUser().getEmail();

            // Check if the user already exists
            Optional<User> existingUser = userRepository.findOneByLogin(login);
            User user;

            if (existingUser.isEmpty()) {
                LOG.debug("User doesnt exist");
                // Use registerUser to create and save a new user
                AdminUserDTO newUserDTO = new AdminUserDTO();
                newUserDTO.setLogin(login);
                newUserDTO.setEmail(email);
                newUserDTO.setActivated(true);

                // Generate a secure default password (hashing is handled in registerUser)
                String defaultPassword = "defaultPassword";
                user = userService.registerUser(newUserDTO, defaultPassword);
                // Save the professor details
                UserDTO UserDTO = userMapper.userToUserDTO(user);
                professorDetail.setUser(UserDTO);
                professorDetailsRepository.save(professorDetailsMapper.toEntity(professorDetail));
            } else {
                LOG.debug("User does exist");
            }
        }

        LOG.debug("Successfully imported professor details from CSV: {}", professorDetailsList);
    }

    private List<ProfessorDetailsDTO> parseCsv(MultipartFile file) throws IOException {
        List<ProfessorDetailsDTO> professorDetailsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Read and discard the header line
            String headerLine = reader.readLine(); // This reads the header and ignores it

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Map CSV data to DTO fields
                ProfessorDetailsDTO professorDetail = new ProfessorDetailsDTO();
                professorDetail.setRank(Rank.valueOf(data[1].trim()));

                UserDTO userDTO = new UserDTO();
                userDTO.setLogin(data[2].trim());
                userDTO.setEmail(data[3].trim());

                professorDetail.setUser(userDTO);

                professorDetailsList.add(professorDetail);
                LOG.debug("professorDetailsList Line: {}", (Object) professorDetailsList);
            }
        }

        return professorDetailsList;
    }

    public void writeProfessorsTemplate(PrintWriter writer) throws IOException {
        // Add CSV header
        writer.println("ID,Role,Login,Email");

        // Optionally add example rows
        writer.println("101,ASSISTANT,12121212,johndoe@example.com");
        writer.println("102,VACATAIRE,13131313,janedoe@example.com");
    }
}
