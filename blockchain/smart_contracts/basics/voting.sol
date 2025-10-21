// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract Voting {
    mapping(address => bool) public hasVoted;
    uint256 public yes;
    uint256 public no;
    address public owner;

    event Voted(address indexed voter, bool choice);

    constructor() {
        owner = msg.sender; // deployer
    }

    function vote(bool choice) external {
        require(!hasVoted[msg.sender], "Already voted");
        hasVoted[msg.sender] = true;
        if (choice) yes += 1; else no += 1;
        emit Voted(msg.sender, choice);
    }

    function totals() external view returns (uint256 _yes, uint256 _no) {
        return (yes, no);
    }
}