package org.votesmart.classes;

import org.votesmart.api.*;
import org.votesmart.data.AddlBio;
import org.votesmart.data.BioOld;
import org.votesmart.data.Bio;

/**
 * <pre>
 * CandidateBio Class
 * 
 * *- Required
 * *- Multiple rows
 * 
 * CandidateBio.getBio()
 * This method grabs the main bio for each candidate.
 * Input: candidateId*
 * Output: {@link BioOld}: Candidate biographical information
 * 
 * CandidateBio.getAddlBio()
 * This method grabs the extended bio for each candidate that has one.
 * Input: candidateId*
 * Output: {@link AddlBio}: Additional items in a list.
 * 
 * ====== EXAMPLE USAGE ========
 * 
 * CandidateBioClass candidateBioClass = new CandidateBioClass();
 * 
 * // Bio of Official
 * Bio bio = candidateBioClass.getBio(candidate.candidateId);
 * </pre> 
 * 
 */
public class CandidateBioClass extends ClassesBase {

	/**
	 * Constructor for testing purposes.
	 * 
	 * @param api
	 */
	public CandidateBioClass(VoteSmartAPI api) {
		super(api);
	}

	/**
	 * Default Constructor
	 */
	public CandidateBioClass() throws VoteSmartException {
		super();
	}

	/**
	 * This method grabs the main bio for each candidate.
	 * 
	 * @param candidateId
	 * @return {@link BioOld}: Candidate biographical information
	 */
/*	
	public BioOld getBio(String candidateId) throws VoteSmartException, VoteSmartErrorException {
		return api.query("CandidateBio.getBio", new ArgMap("candidateId", candidateId), BioOld.class );
	}
*/	
	/**
	 * This method grabs the extended bio for each candidate that has one.
	 * 
	 * @param candidateId
	 * @return {@link AddlBio}: Additional items in a list.
	 */
	public AddlBio getAddlBio(String candidateId) throws VoteSmartException, VoteSmartErrorException {
		return api.query("CandidateBio.getAddlBio", new ArgMap("candidateId", candidateId), AddlBio.class );
	}
	
	/**
	 * This method grabs the extended bio for each candidate that has one. 
	 * It uses the updated version 'getDetailedBio' of the call.
	 * 
	 * @param candidateId
	 * @return {@link AddlBio}: Additional items in a list.
	 */
	public Bio getBio(String candidateId) throws VoteSmartException, VoteSmartErrorException {
		return api.query("CandidateBio.getDetailedBio", new ArgMap("candidateId", candidateId), Bio.class );
	}
}
